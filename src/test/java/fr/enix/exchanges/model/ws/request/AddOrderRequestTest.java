package fr.enix.exchanges.model.ws.request;

import fr.enix.common.service.EncryptionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.util.UriUtils;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class AddOrderRequestTest {

    @Autowired
    EncryptionService encryptionService;

    @Test
    void testGetQueryParametersRepresentation_shouldBuildLeverageAndClose() {
        final String            nonce           = encryptionService.getNewNonce();
        final AddOrderRequest   addOrderRequest = AddOrderRequest
                                                                .builder    ()
                                                                .nonce      (nonce)
                                                                .pair       ("XLTCZEUR")
                                                                .type       ("buy")
                                                                .ordertype  ("stop-loss")
                                                                .price      (new BigDecimal("40"))
                                                                .volume     (new BigDecimal("1"))
                                                                .close      (AddOrderRequest.Close
                                                                        .builder    ()
                                                                        .ordertype  ("stop-loss")
                                                                        .price      (new BigDecimal("39"))
                                                                        .build      ()
                                                                )
                                                                .build      ();
        assertEquals(
                UriUtils.encodePath(
                    new StringBuilder("nonce"           ).append("=").append(nonce              ).append("&")
                              .append("pair"            ).append("=").append("XLTCZEUR"         ).append("&")
                              .append("type"            ).append("=").append("buy"              ).append("&")
                              .append("ordertype"       ).append("=").append("stop-loss"            ).append("&")
                              .append("volume"          ).append("=").append(1                  ).append("&")
                              .append("price"           ).append("=").append(40                 ).append("&")
                              .append("close[ordertype]").append("=").append("stop-loss" ).append("&")
                              .append("close[price]"    ).append("=").append(39              ).toString(),
                            "UTF-8"
                ),
                addOrderRequest.getQueryParametersRepresentation()


        );
    }
}
