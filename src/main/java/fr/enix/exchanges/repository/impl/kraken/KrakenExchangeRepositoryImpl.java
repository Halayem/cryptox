package fr.enix.exchanges.repository.impl.kraken;

import fr.enix.common.exception.KrakenExceptionFactoryProvider;
import fr.enix.common.service.EncryptionService;
import fr.enix.exchanges.mapper.AddOrderMapper;
import fr.enix.exchanges.model.business.input.AddOrderInput;
import fr.enix.exchanges.model.business.output.AddOrderOutput;
import fr.enix.exchanges.model.parameters.AddOrderType;
import fr.enix.exchanges.model.parameters.AssetClass;
import fr.enix.exchanges.model.repository.ApplicationRepositoryProperties;
import fr.enix.exchanges.model.ws.request.AddOrderRequest;
import fr.enix.exchanges.model.ws.request.NonceRequest;
import fr.enix.exchanges.model.ws.request.TradeBalanceRequest;
import fr.enix.exchanges.model.ws.response.AddOrderResponse;
import fr.enix.exchanges.model.ws.response.BalanceResponse;
import fr.enix.exchanges.model.ws.response.ErrorResponse;
import fr.enix.exchanges.model.ws.response.OpenOrdersResponse;
import fr.enix.exchanges.repository.ExchangeRepository;
import fr.enix.exchanges.service.CurrenciesRepresentation;
import fr.enix.exchanges.service.CurrenciesRepresentationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Map;

@AllArgsConstructor
@Slf4j
public class KrakenExchangeRepositoryImpl implements ExchangeRepository {

    private final WebClient exchangeWebClient;
    private final EncryptionService encryptionService;
    private final AddOrderMapper addOrderMapper;
    private final CurrenciesRepresentationService currenciesRepresentationService;
    private final ApplicationRepositoryProperties applicationRepositoryProperties;

    @Override
    public Mono<BigDecimal> getAvailableAssetForSellPlacementByApplicationAssetPair(final String applicationAssetPair) {
        return  getBalanceByApplicationAsset(
                    extractApplicationAssetWhenIsSellOperation(applicationAssetPair)
                )
                .zipWith(getTotalSellOpenOrders(applicationAssetPair))
                .map(objects -> {
                    final BigDecimal balance                = objects.getT1();
                    final BigDecimal totalSellOpenOrders    = objects.getT2();

                    return balance.subtract(totalSellOpenOrders);
                });
    }

    @Override
    public Mono<BigDecimal> getAvailableAssetForBuyPlacementByApplicationAssetPair(final String applicationAssetPair) {
        return  getBalanceByApplicationAsset(
                    extractApplicationAssetWhenIsBuyOperation(applicationAssetPair)
                )
                .zipWith(getTotalBuyOpenOrders(applicationAssetPair))
                .map(objects -> {
                    final BigDecimal balance                = objects.getT1();
                    final BigDecimal totalSellOpenOrders    = objects.getT2();

                    return balance.subtract(totalSellOpenOrders);
                });
    }

    @Override
    public Mono<AddOrderOutput> addOrder(final AddOrderInput addOrderInput) {
        final AddOrderRequest addOrderRequest = addOrderMapper.mapAddOrderBusinessToAddOrderRequest( addOrderInput, encryptionService.getNewNonce() );

        return
            executeWebClientMono(
                applicationRepositoryProperties.getWebservice().getUrn().get("addOrder"),
                addOrderRequest.getQueryParametersRepresentation(),
                addOrderRequest.getNonce(),
                AddOrderResponse.class
            )
            .map(addOrderMapper::mapAddOrderResponseToAddOrderOutput);
    }

    protected Mono<BigDecimal> getTotalSellOpenOrders(final String applicationAssetPair) {
        return this.getTotalPlacementsForApplicationAssetPairByOrderType(applicationAssetPair, AddOrderType.SELL);
    }

    protected Mono<BigDecimal> getTotalBuyOpenOrders(final String applicationAssetPair) {
        return this.getTotalPlacementsForApplicationAssetPairByOrderType(applicationAssetPair, AddOrderType.BUY);
    }

    protected Mono<BigDecimal> getBalanceByApplicationAsset(final String applicationAsset) {
        final NonceRequest nonceRequest = NonceRequest.builder    ()
                                                      .nonce      (encryptionService.getNewNonce())
                                                      .build      ();
        return
            executeWebClientMono(
                applicationRepositoryProperties.getWebservice().getUrn().get("balance"),
                nonceRequest.getQueryParametersRepresentation(),
                nonceRequest.getNonce(),
                BalanceResponse.class
            )
            .map(balanceResponse ->
                balanceResponse
                .getResult()
                .get(CurrenciesRepresentation.valueOf(applicationAsset.toUpperCase()).getKrakenWebServiceRepresentation())
            );
    }

    protected Flux<String> getTradeBalance(final AssetClass assetClass) {
        final TradeBalanceRequest tradeBalanceRequest = TradeBalanceRequest.builder ()
                .nonce   (encryptionService.getNewNonce())
                .aclass  (assetClass.getValue())
                .build   ();
        return
                executeWebClient(
                    applicationRepositoryProperties.getWebservice().getUrn().get("tradeBalance"),
                    tradeBalanceRequest.getQueryParametersRepresentation(),
                    tradeBalanceRequest.getNonce(),
                    String.class
                );
    }

    private Mono<BigDecimal> getTotalPlacementsForApplicationAssetPairByOrderType(final String applicationAssetPair,
                                                                                  final AddOrderType addOrderType) {

        final String krakenOrderType                = addOrderType.getKrakenOrderType();
        final String krakenAssetPairRepresentation  = currenciesRepresentationService.getAssetPairCurrencyWithoutPrefixAndWithoutSeparatorRepresentationByApplicationAssetPair(applicationAssetPair);
        final OpenOrderFilter openOrderFilter       = new OpenOrderFilter(krakenAssetPairRepresentation, krakenOrderType);

        return
        getOpenOrders()
        .map(openOrdersResponse -> openOrdersResponse.getResult().getOpen())
        .map(openOrders ->
            openOrders
            .entrySet   ()
            .stream     ()
            .filter     (entry -> openOrderFilter.entryMatch(entry) )
            .map        (entry -> getPlacementOneOrderByOrderType(entry.getValue(), krakenOrderType))
            .reduce     (BigDecimal.ZERO, BigDecimal::add)
        );
    }

    private BigDecimal getPlacementOneOrderByOrderType(final OpenOrdersResponse.Order order,
                                                       final String orderType) {
        switch (orderType) {
            case "buy":     return order.getVol().multiply(order.getDescr().getPrice());
            case "sell":    return order.getVol();
            default:        throw new IllegalArgumentException("unhandled kraken order type: " + orderType);
        }
    }

    protected Mono<OpenOrdersResponse> getOpenOrders() {
        final NonceRequest nonceRequest = NonceRequest.builder    ()
                                                      .nonce      (encryptionService.getNewNonce())
                                                      .build      ();

        return executeWebClientMono(
                    applicationRepositoryProperties.getWebservice().getUrn().get("openOrders"),
                    nonceRequest.getQueryParametersRepresentation(),
                    nonceRequest.getNonce(),
                    OpenOrdersResponse.class
                );
    }

    private <T> Flux<T> executeWebClient(String uri, String query, String nonce, Class<T> clazz) {
        return
                exchangeWebClient
                        .post       ()
                        .uri        (uri)
                        .body       (BodyInserters.fromPublisher(Mono.just(query), String.class))
                        .headers    (httpHeaders -> httpHeaders.set("API-Sign", encryptionService.getHmacDigest(nonce, query, uri )))
                        .retrieve   ()
                        .bodyToFlux (clazz)
                        .doOnNext   (response -> checkKrakenBodyResponse((ErrorResponse)response));
    }

    private <T> Mono<T> executeWebClientMono(String uri, String query, String nonce, Class<T> type) {
        return
                exchangeWebClient
                        .post       ()
                        .uri        (uri)
                        .body       (BodyInserters.fromPublisher(Mono.just(query), String.class))
                        .headers    (httpHeaders -> httpHeaders.set("API-Sign", encryptionService.getHmacDigest(nonce, query, uri )))
                        .retrieve   ()
                        .bodyToMono (type)
                        .doOnNext   (response -> checkKrakenBodyResponse((ErrorResponse)response));
    }

    private void checkKrakenBodyResponse(final ErrorResponse errorResponse) {
        if ( !errorResponse.getError().isEmpty() ) {
            log.error("received error message(s) from Kraken: {}, trying to throw the appropriate exception", errorResponse.getError());
            throw KrakenExceptionFactoryProvider.getFactory         (errorResponse.getError().get( 0 ))
                                                .getKrakenException ();
        }
    }

    /**
     * when input is "litecoin-euro" then return litecoin
     */
    private String extractApplicationAssetWhenIsSellOperation(final String applicationAssetPair) {
        return applicationAssetPair.split("-")[0];
    }

    /**
     * when input is "litecoin-euro" then return euro
     */
    private String extractApplicationAssetWhenIsBuyOperation(final String applicationAssetPair) {
        return applicationAssetPair.split("-")[1];
    }

    @AllArgsConstructor
    private static class OpenOrderFilter {

        private final String marketAssetPair;
        private final String orderType;

        public boolean entryMatch(final Map.Entry<String, OpenOrdersResponse.Order> entry) {
            return
            (
                orderType.equals        (entry.getValue().getDescr().getType()) &&
                marketAssetPair.equals  (entry.getValue().getDescr().getPair())
            );
        }
    }

}
