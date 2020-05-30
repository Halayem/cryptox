package fr.enix.exchanges.repository.impl;

import fr.enix.common.service.KrakenRepositoryService;
import fr.enix.exchanges.model.business.AddOrderInput;
import fr.enix.exchanges.model.ws.request.AddOrderRequest;
import fr.enix.exchanges.model.ws.request.BalanceRequest;
import fr.enix.exchanges.model.ws.request.TradeBalanceRequest;
import fr.enix.exchanges.model.ws.response.AddOrderResponse;
import fr.enix.exchanges.model.ws.response.BalanceResponse;
import fr.enix.exchanges.repository.KrakenPrivateRepository;
import fr.enix.kraken.AssetClass;
import fr.enix.mapper.AddOrderMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Slf4j
public class KrakenPrivateRepositoryImpl implements KrakenPrivateRepository {

    private final WebClient krakenPrivateWebClient;
    private final KrakenRepositoryService krakenRepositoryService;
    private final AddOrderMapper addOrderMapper;

    private final String balanceUri = "/0/private/Balance";

    @Override
    public Flux<BalanceResponse> getBalance() {
        final BalanceRequest balanceRequest = BalanceRequest.builder    ()
                                                            .nonce      (krakenRepositoryService.getNewNonce())
                                                            .build      ();
        return krakenPrivateWebClient
                .post       ()
                .uri        (balanceUri)
                .body       (BodyInserters.fromPublisher(
                                Mono.just(balanceRequest.getQueryParametersRepresentation()), String.class
                            )
                )
                .headers    (httpHeaders -> {
                    httpHeaders.set("API-Sign",
                                    krakenRepositoryService.getHmacDigest(
                                        balanceRequest.getNonce(),
                                        balanceRequest.getQueryParametersRepresentation(),
                                        balanceUri
                                    ));
                })
                .retrieve   ()
                .bodyToFlux (BalanceResponse.class);
    }


    private final String tradeBalanceUri = "/0/private/TradeBalance";
    @Override
    public Flux<String> getTradeBalance(final AssetClass assetClass) {
        final TradeBalanceRequest tradeBalanceRequest = TradeBalanceRequest.builder ()
                                                                           .nonce   (krakenRepositoryService.getNewNonce())
                                                                           .aclass  (assetClass.getValue())
                                                                           .build   ();
        return krakenPrivateWebClient
                .post       ()
                .uri        (tradeBalanceUri)
                .body       (BodyInserters.fromPublisher(
                                Mono.just(tradeBalanceRequest.getQueryParametersRepresentation()), String.class
                            )
                )
                .headers    (httpHeaders -> {
                    httpHeaders.set("API-Sign",
                                    krakenRepositoryService.getHmacDigest(
                                        tradeBalanceRequest.getNonce(),
                                        tradeBalanceRequest.getQueryParametersRepresentation(),
                                        tradeBalanceUri
                                    ));
                })
                .retrieve   ()
                .bodyToFlux (String.class);
    }


    private final String addOrderUri = "/0/private/AddOrder";

    @Override
    public Flux<AddOrderResponse> addOrder(final AddOrderInput addOrderInput) {
        final AddOrderRequest addOrderRequest = addOrderMapper.mapAddOrderBusinessToAddOrderRequest(
                addOrderInput,
                                                    krakenRepositoryService.getNewNonce());
        return krakenPrivateWebClient
                .post       ()
                .uri        (addOrderUri)
                .body       (BodyInserters.fromPublisher(
                                Mono.just(addOrderRequest.getQueryParametersRepresentation()), String.class
                            )
                )
                .headers    (httpHeaders -> {
                    httpHeaders.set("API-Sign",
                                    krakenRepositoryService.getHmacDigest(
                                        addOrderRequest.getNonce(),
                                        addOrderRequest.getQueryParametersRepresentation(),
                                        addOrderUri
                                    ));
                })
                .retrieve   ()
                .bodyToFlux (AddOrderResponse.class);
    }

}
