package fr.enix.exchanges.model.ws.request;

import fr.enix.common.service.KrakenRepositoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.util.UriUtils;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class AddOrderRequestTest {

    @Autowired KrakenRepositoryService krakenRepositoryService;

    @Test
    public void testGetQueryParametersRepresentation_shouldBuildLeverageAndClose() {
        final String            nonce           = krakenRepositoryService.getNewNonce();
        final AddOrderRequest   addOrderRequest = AddOrderRequest
                                                                .builder    ()
                                                                .nonce      (nonce)
                                                                .pair       ("XLTCZEUR")
                                                                .type       ("buy")
                                                                .ordertype  ("limit")
                                                                .price      (new BigDecimal("40"))
                                                                .volume     (new BigDecimal("1"))
                                                                .leverage   ("2:1")
                                                                .close      (AddOrderRequest.Close
                                                                        .builder    ()
                                                                        .ordertype  ("stop-loss-profit")
                                                                        .price      ("#5%")
                                                                        .price2     ("#10")
                                                                        .build      ()
                                                                )
                                                                .build      ();
        assertEquals(
                UriUtils.encodePath(
                    new StringBuilder("nonce"           ).append("=").append(nonce              ).append("&")
                              .append("pair"            ).append("=").append("XLTCZEUR"         ).append("&")
                              .append("type"            ).append("=").append("buy"              ).append("&")
                              .append("ordertype"       ).append("=").append("limit"            ).append("&")
                              .append("price"           ).append("=").append(40                 ).append("&")
                              .append("volume"          ).append("=").append(1                  ).append("&")
                              .append("leverage"        ).append("=").append("2:1"              ).append("&")
                              .append("close[ordertype]").append("=").append("stop-loss-profit" ).append("&")
                              .append("close[price]"    ).append("=").append("#5%"              ).append("&")
                              .append("close[price2]"   ).append("=").append("#10"              ).toString(),
                            "UTF-8"
                ),
                addOrderRequest.getQueryParametersRepresentation()


        );
    }
}
