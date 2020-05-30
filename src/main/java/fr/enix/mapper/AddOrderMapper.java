package fr.enix.mapper;

import fr.enix.exchanges.model.business.AddOrderInput;
import fr.enix.exchanges.model.business.AddOrderOutput;
import fr.enix.exchanges.model.ws.request.AddOrderRequest;
import fr.enix.exchanges.model.ws.response.AddOrderResponse;

public class AddOrderMapper {

    public AddOrderRequest mapAddOrderBusinessToAddOrderRequest(final AddOrderInput addOrderInput,
                                                                final String nonce) {
        return AddOrderRequest.builder  ()
                              .nonce    (nonce)
                              .pair     (addOrderInput.getAssetPair().getCode())
                              .type     (addOrderInput.getAddOrderType().getValue())
                              .ordertype(addOrderInput.getOrderType().getValue())
                              .price    (addOrderInput.getPrice())
                              .volume   (addOrderInput.getVolume())
                              .build    ();
    }

    public AddOrderOutput mapAddOrderResponseToAddOrderOutput(final AddOrderResponse addOrderResponse) {
        return  AddOrderOutput.builder()
                              .errors           (addOrderResponse.getError())
                              .description      (addOrderResponse.getResult().getDescr().getOrder())
                              .transactionIds   (addOrderResponse.getResult().getTxid())
                              .build            ();
    }
}
