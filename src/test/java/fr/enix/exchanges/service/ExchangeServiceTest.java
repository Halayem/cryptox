package fr.enix.exchanges.service;

import com.github.tomakehurst.wiremock.WireMockServer;
import fr.enix.common.exception.eapi.KrakenEapiInvalidKeyException;
import fr.enix.exchanges.model.business.input.AddOrderInput;
import fr.enix.exchanges.model.parameters.*;
import fr.enix.exchanges.model.websocket.AssetPair;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class ExchangeServiceTest {

    @Autowired
    private ExchangeService exchangeService;

    @Test
    @Order(0)
    public void testGetBalance_success() {
        StepVerifier.create         ( exchangeService.getBalance() )
                    .consumeNextWith( balanceResponse -> {
                        assertEquals( new BigDecimal("2.48"),  balanceResponse.getResult().get( XzAsset.XLTC  ));
                        assertEquals( new BigDecimal("40.25"), balanceResponse.getResult().get( XzAsset.ZEUR ));

                        assertTrue( balanceResponse.getError().size() == 0 );

                    })
                    .verifyComplete ();
    }

    @Test
    @Order(1)
    public void testGetBalance_eapiInvalidKey() {
        StepVerifier.create ( exchangeService.getBalance() )
                .expectError( KrakenEapiInvalidKeyException.class )
                .verify     ();
    }

    @Test
    public void testAddOrder_success() {
        StepVerifier.create(exchangeService.addOrder    ( AddOrderInput.builder     ()
                                                                       .assetPair   (AssetPair.builder  ()
                                                                                              .from     (XzAsset.XLTC)
                                                                                              .to       (XzAsset.ZEUR)
                                                                                              .build    ()
                                                                       )
                                                                       .addOrderType(AddOrderType.SELL)
                                                                       .orderType   (OrderType.LIMIT)
                                                                       .price       (new BigDecimal(40  ))
                                                                       .volume      (new BigDecimal(1   ))
                                                                       .build       ()
                                                        )
                    )
                    .consumeNextWith(addOrderOutput -> {
                        assertEquals("sell 1.00000000 LTCEUR @ limit 40.00", addOrderOutput.getDescription());
                        assertTrue(addOrderOutput.getTransactionIds().contains("OA6DK2-UK35J-NYDBAF"));
                        assertTrue(addOrderOutput.getTransactionIds().size() == 1 );
                        assertTrue( addOrderOutput.getErrors().size() == 0 );
                    })
                    .verifyComplete();
    }

    @Test
    public void testOpenOrders_success() {
        StepVerifier.create         (exchangeService.getOpenOrders())
                    .consumeNextWith(openOrderOutput -> {
                        assertEquals("O7AHQZ-MAJTT-NIAZWV",    openOrderOutput.getTransactionId()  );
                        assertEquals(new BigDecimal("43.00"),       openOrderOutput.getPrice()          );
                        assertEquals(new BigDecimal("3.50143000"),  openOrderOutput.getVolume()         );
                        assertEquals(AddOrderType.SELL,                 openOrderOutput.getOrderType()      );
                        assertEquals(Status.OPEN,                       openOrderOutput.getStatus()         );
                    })
                    .consumeNextWith(openOrderOutput -> {
                        assertEquals("OIM6VX-CCEKP-7DBOKM",    openOrderOutput.getTransactionId()  );
                        assertEquals(new BigDecimal("35.02"),       openOrderOutput.getPrice()          );
                        assertEquals(new BigDecimal("0.50000000"),  openOrderOutput.getVolume()         );
                        assertEquals(AddOrderType.BUY,                  openOrderOutput.getOrderType()      );
                        assertEquals(Status.OPEN,                       openOrderOutput.getStatus()         );
                    })
                    .consumeNextWith(openOrderOutput -> {
                        assertEquals("OPW54R-RZGFZ-EAGNT3",    openOrderOutput.getTransactionId()  );
                        assertEquals(new BigDecimal("35.20"),       openOrderOutput.getPrice()          );
                        assertEquals(new BigDecimal("0.40000000"),  openOrderOutput.getVolume()         );
                        assertEquals(AddOrderType.BUY,                  openOrderOutput.getOrderType()      );
                        assertEquals(Status.OPEN,                       openOrderOutput.getStatus()         );
                    })
                    .verifyComplete();
    }

    @Test
    public void testGetTotalBuyPlacements_success() {
        StepVerifier.create(
            exchangeService.getTotalBuyPlacements(fr.enix.exchanges.model.ws.AssetPair.builder  ()
                                                                                      .from     (Asset.LTC)
                                                                                      .to       (Asset.EUR)
                                                                                      .build    ())
        )
        .consumeNextWith(totalBuyPlacements -> {
            assertEquals(new BigDecimal("31.5900000000"), totalBuyPlacements);
        })
        .verifyComplete();
    }

    @Test
    public void testGetTotalSellPlacements_success() {
        StepVerifier.create(
                exchangeService.getTotalSellPlacements(fr.enix.exchanges.model.ws.AssetPair.builder  ()
                                                                                           .from     (Asset.LTC)
                                                                                           .to       (Asset.EUR)
                                                                                           .build    ())
        )
        .consumeNextWith(totalSellPlacements -> {
            assertEquals(new BigDecimal("150.5614900000"), totalSellPlacements);
        })
        .verifyComplete();
    }

    @BeforeAll
    public static void startWiremockServer() throws InterruptedException {
        new WireMockServer().start();
    }
}
