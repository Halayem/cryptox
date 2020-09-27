package fr.enix.exchanges.mapper;

import fr.enix.common.service.KrakenRepositoryService;
import fr.enix.exchanges.model.business.input.AddOrderInput;
import fr.enix.exchanges.model.parameters.AddOrderType;
import fr.enix.exchanges.model.parameters.OrderType;
import fr.enix.exchanges.model.ws.request.AddOrderRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class AddOrderMapperTest {

    @Autowired AddOrderMapper           addOrderMapper;
    @Autowired KrakenRepositoryService  krakenRepositoryService;

    @Test
    void testAddOrderMapper_leverageAndCloseShouldBeNull() {
        final String nonce = krakenRepositoryService.getNewNonce();
        assertEquals(
                AddOrderRequest
                    .builder    ()
                    .nonce      (nonce)
                    .pair       ("XLTCZEUR")
                    .type       ("sell")
                    .ordertype  ("limit")
                    .price      (new BigDecimal("40"))
                    .volume     (new BigDecimal("1"))
                    .build      (),
                addOrderMapper.mapAddOrderBusinessToAddOrderRequest(
                    AddOrderInput
                        .builder                ()
                        .applicationAssetPair   ("litecoin-euro")
                        .addOrderType           (AddOrderType.SELL)
                        .orderType              (OrderType.LIMIT)
                        .price                  (new BigDecimal(40  ))
                        .volume                 (new BigDecimal(1   ))
                        .build                  (),
                    nonce
                )
        );
    }

    @Test
    void testAddOrderMapper_leverageAndCloseShouldBeSet() {
        final String nonce = krakenRepositoryService.getNewNonce();
        assertEquals(
                AddOrderRequest
                    .builder    ()
                    .nonce      (nonce)
                    .pair       ("XLTCZEUR")
                    .type       ("buy")
                    .ordertype  ("limit")
                    .price      (new BigDecimal("40"))
                    .volume     (new BigDecimal("1"))
                    .leverage   ("2:1")
                    .close      (
                        AddOrderRequest
                        .Close
                        .builder    ()
                        .ordertype  ("stop-loss-profit")
                        .price      ("#5%")
                        .price2     ("#10")
                        .build      ()
                    )
                    .build      (),
                addOrderMapper.mapAddOrderBusinessToAddOrderRequest(
                        AddOrderInput
                            .builder   ()
                            .applicationAssetPair ("litecoin-euro")
                            .addOrderType   (AddOrderType.BUY)
                            .orderType      (OrderType.LIMIT)
                            .price          (new BigDecimal(40  ))
                            .volume         (new BigDecimal(1   ))
                            .leverage       ("2:1")
                            .close          (
                                AddOrderInput
                                    .Close
                                    .builder()
                                    .orderType(OrderType.STOP_LOSS_PROFIT)
                                    .stopLossPriceRelativePercentageDelta   ( 5  )
                                    .takeProfitPriceRelativeDelta           ( 10 )
                                    .build())
                            .build(),
                        nonce
                )
        );
    }
}
