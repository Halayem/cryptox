package fr.enix.mapper;

import fr.enix.exchanges.model.business.AddOrder;
import fr.enix.exchanges.model.ws.request.AddOrderRequest;

public class AddOrderMapper {

    public AddOrderRequest mapAddOrderBusinessToAddOrderRequest(final AddOrder addOrder,
                                                                final String nonce) {
        return AddOrderRequest.builder  ()
                              .nonce    (nonce)
                              .pair     (addOrder.getAssetPair().getCode())
                              .type     (addOrder.getAddOrderType().getValue())
                              .ordertype(addOrder.getOrderType().getValue())
                              .price    (addOrder.getPrice())
                              .volume   (addOrder.getVolume())
                              .build    ();
    }
}
