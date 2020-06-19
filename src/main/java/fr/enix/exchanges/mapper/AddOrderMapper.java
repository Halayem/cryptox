package fr.enix.exchanges.mapper;

import fr.enix.exchanges.model.business.input.AddOrderInput;
import fr.enix.exchanges.model.business.output.AddOrderOutput;
import fr.enix.exchanges.model.ws.request.AddOrderRequest;
import fr.enix.exchanges.model.ws.response.AddOrderResponse;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AddOrderMapper {

    private final AssetMapper assetMapper;

    public AddOrderRequest mapAddOrderBusinessToAddOrderRequest(final AddOrderInput addOrderInput,
                                                                final String nonce) {
        return AddOrderRequest.builder  ()
                              .nonce    (nonce)
                              .pair     (assetMapper.mapAssetPairForWebService(addOrderInput.getAssetPair()))
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
