package fr.enix.exchanges.repository.impl;

import fr.enix.common.exception.KrakenExceptionFactoryProvider;
import fr.enix.common.service.KrakenRepositoryService;
import fr.enix.exchanges.model.business.input.AddOrderInput;
import fr.enix.exchanges.model.ws.request.AddOrderRequest;
import fr.enix.exchanges.model.ws.request.NonceRequest;
import fr.enix.exchanges.model.ws.request.TradeBalanceRequest;
import fr.enix.exchanges.model.ws.response.AddOrderResponse;
import fr.enix.exchanges.model.ws.response.BalanceResponse;
import fr.enix.exchanges.model.ws.response.ErrorResponse;
import fr.enix.exchanges.model.ws.response.OpenOrdersResponse;
import fr.enix.exchanges.repository.KrakenPrivateRepository;
import fr.enix.exchanges.model.parameters.AssetClass;
import fr.enix.exchanges.mapper.AddOrderMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Slf4j
public class KrakenPrivateRepositoryImpl implements KrakenPrivateRepository {

    private final WebClient                 krakenPrivateWebClient;
    private final KrakenRepositoryService   krakenRepositoryService;
    private final AddOrderMapper            addOrderMapper;

    private final String openOrdersUri      = "/0/private/OpenOrders";
    private final String balanceUri         = "/0/private/Balance";
    private final String tradeBalanceUri    = "/0/private/TradeBalance";
    private final String addOrderUri        = "/0/private/AddOrder";

    @Override
    public Mono<OpenOrdersResponse> getOpenOrders() {
        final NonceRequest nonceRequest = NonceRequest.builder    ()
                                                      .nonce      (krakenRepositoryService.getNewNonce())
                                                      .build      ();

        return executeWebClientMono(
                    openOrdersUri,
                    nonceRequest.getQueryParametersRepresentation(),
                    nonceRequest.getNonce(),
                    OpenOrdersResponse.class
        );
    }

    @Override
    public Flux<BalanceResponse> getBalance() {
        final NonceRequest nonceRequest = NonceRequest.builder    ()
                                                      .nonce      (krakenRepositoryService.getNewNonce())
                                                      .build      ();
        return executeWebClient(balanceUri,
                                nonceRequest.getQueryParametersRepresentation(),
                                nonceRequest.getNonce(),
                                BalanceResponse.class);
    }

    @Override
    public Flux<String> getTradeBalance(final AssetClass assetClass) {
        final TradeBalanceRequest tradeBalanceRequest = TradeBalanceRequest.builder ()
                                                                           .nonce   (krakenRepositoryService.getNewNonce())
                                                                           .aclass  (assetClass.getValue())
                                                                           .build   ();
        return executeWebClient(tradeBalanceUri,
                                tradeBalanceRequest.getQueryParametersRepresentation(),
                                tradeBalanceRequest.getNonce(),
                                String.class);
    }

    @Override
    public Flux<AddOrderResponse> addOrder(final AddOrderInput addOrderInput) {
        final AddOrderRequest addOrderRequest = addOrderMapper.mapAddOrderBusinessToAddOrderRequest(
                                                        addOrderInput, krakenRepositoryService.getNewNonce()
                                                );
        return executeWebClient(addOrderUri,
                                addOrderRequest.getQueryParametersRepresentation(),
                                addOrderRequest.getNonce(),
                                AddOrderResponse.class);
    }

    private Flux executeWebClient(String uri, String query, String nonce, Class clazz) {
        return
                krakenPrivateWebClient
                        .post       ()
                        .uri        (uri)
                        .body       (BodyInserters.fromPublisher(Mono.just(query), String.class))
                        .headers    (httpHeaders -> httpHeaders.set("API-Sign",krakenRepositoryService.getHmacDigest(nonce, query, uri )))
                        .retrieve   ()
                        .bodyToFlux (clazz)
                        .doOnNext   (response -> checkKrakenBodyResponse((ErrorResponse)response));
    }

    private Mono executeWebClientMono(String uri, String query, String nonce, Class clazz) {
        return
                krakenPrivateWebClient
                        .post       ()
                        .uri        (uri)
                        .body       (BodyInserters.fromPublisher(Mono.just(query), String.class))
                        .headers    (httpHeaders -> httpHeaders.set("API-Sign",krakenRepositoryService.getHmacDigest(nonce, query, uri )))
                        .retrieve   ()
                        .bodyToMono (clazz)
                        .doOnNext   (response -> checkKrakenBodyResponse((ErrorResponse)response));
    }

    private void checkKrakenBodyResponse(final ErrorResponse errorResponse) {
        if ( !errorResponse.getError().isEmpty() ) {
            log.error("received error message(s) from Kraken: {}, trying to throw the appropriate exception", errorResponse.getError());
            throw KrakenExceptionFactoryProvider.getFactory         (errorResponse.getError().get( 0 ))
                                                .getKrakenException ();
        }
    }

}
