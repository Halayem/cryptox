package fr.enix.exchanges.mapper;

import fr.enix.exchanges.model.business.input.AddOrderInput;
import fr.enix.exchanges.model.business.output.AddOrderOutput;
import fr.enix.exchanges.model.parameters.AddOrderType;
import fr.enix.exchanges.model.parameters.OrderType;
import fr.enix.exchanges.model.ws.request.AddOrderRequest;
import fr.enix.exchanges.model.ws.response.AddOrderResponse;
import fr.enix.exchanges.service.CurrenciesRepresentationService;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@AllArgsConstructor
public class AddOrderMapper {

    private final CurrenciesRepresentationService currenciesRepresentationService;

    public AddOrderRequest mapAddOrderBusinessToAddOrderRequest(final AddOrderInput addOrderInput,
                                                                final String nonce) {
        AddOrderRequest.AddOrderRequestBuilder addOrderRequestBuilder =
                AddOrderRequest.builder  ()
                               .nonce    (nonce)
                               .pair     (currenciesRepresentationService.getAssetPairCurrencyWebServiceRepresentationByApplicationAssetPair(addOrderInput.getApplicationAssetPair()))
                               .type     (addOrderInput.getAddOrderType().getKrakenOrderType())
                               .ordertype(addOrderInput.getOrderType().getValue())
                               .volume   (addOrderInput.getVolume())
                               .price    (addOrderInput.getPrice());

        if ( addOrderInput.getLeverage() != null ) {
            addOrderRequestBuilder.leverage(addOrderInput.getLeverage());
        }

        if ( addOrderInput.getClose() != null ) {
            addOrderRequestBuilder.close(
              AddOrderRequest.Close.builder     ()
                                   .ordertype   (addOrderInput.getClose().getOrderType().getValue())
                                   .price       ( addOrderInput.getClose().getStopLossPrice())
                                   .build       ()
            );
        }

        return addOrderRequestBuilder.build();
    }

    public AddOrderOutput mapAddOrderResponseToAddOrderOutput(final AddOrderResponse addOrderResponse) {
        return  AddOrderOutput.builder()
                              .errors           (addOrderResponse.getError())
                              .description      (addOrderResponse.getResult().getDescr().getOrder())
                              .transactionIds   (addOrderResponse.getResult().getTxid())
                              .build            ();
    }

    public Mono<AddOrderInput> newAddOrderInputForBuyPlacement(final String     applicationAssetPair,
                                                               final BigDecimal amountToBuy,
                                                               final BigDecimal priceToBuy) {
        return Mono.just(
                AddOrderInput
                        .builder                ()
                        .applicationAssetPair   (applicationAssetPair   )
                        .addOrderType           (AddOrderType.BUY       )
                        .orderType              (OrderType.LIMIT        )
                        .price                  (priceToBuy             )
                        .volume                 (amountToBuy            )
                        .build()
        );
    }

    public Mono<AddOrderInput> newAddOrderInputForBuyPlacementWithStopLoss(final String     applicationAssetPair,
                                                                           final BigDecimal amountToBuy,
                                                                           final BigDecimal priceToBuy,
                                                                           final BigDecimal buyStopLossPrice) {
        return Mono.just(
                AddOrderInput
                        .builder                ()
                        .applicationAssetPair   ( applicationAssetPair   )
                        .addOrderType           ( AddOrderType.BUY       )
                        .orderType              ( OrderType.STOP_LOSS    )
                        .price                  ( priceToBuy             )
                        .volume                 ( amountToBuy            )
                        .close                  ( AddOrderInput.Close.builder       ()
                                                                     .orderType     ( OrderType.STOP_LOSS )
                                                                     .stopLossPrice ( buyStopLossPrice )
                                                                     .build         ()
                        )
                        .build()
        );
    }

    public Mono<AddOrderInput> newAddOrderInputForSellPlacement(final String        applicationAssetPair,
                                                                final BigDecimal    amountToSell,
                                                                final BigDecimal    priceToSell) {
        return Mono.just(
                AddOrderInput
                        .builder                ()
                        .applicationAssetPair   (applicationAssetPair   )
                        .addOrderType           (AddOrderType.SELL      )
                        .orderType              (OrderType.LIMIT        )
                        .price                  (priceToSell            )
                        .volume                 (amountToSell           )
                        .build()
        );
    }
}
