package fr.enix.exchanges.service;

import com.github.tomakehurst.wiremock.WireMockServer;
import fr.enix.common.exception.eapi.KrakenEapiInvalidKeyException;
import fr.enix.exchanges.model.business.input.AddOrderInput;
import fr.enix.exchanges.model.parameters.*;
import fr.enix.exchanges.model.websocket.AssetPair;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ExchangeServiceTest {

    @Autowired
    private ExchangeService exchangeService;

    @Test
    @Order(0)
    void testGetBalance_success() {
        StepVerifier.create         ( exchangeService.getBalance() )
                    .consumeNextWith( balanceResponse -> {
                        assertEquals( new BigDecimal("8.48"),  balanceResponse.getResult().get( XzAsset.XLTC  ));
                        assertEquals( new BigDecimal("40.25"), balanceResponse.getResult().get( XzAsset.ZEUR ));

                        assertEquals(0, balanceResponse.getError().size() );

                    })
                    .verifyComplete ();
    }


    @Test
    @Order(1)
    void testGetBalance_eapiInvalidKey() {
        StepVerifier.create ( exchangeService.getBalance() )
                .expectError( KrakenEapiInvalidKeyException.class )
                .verify     ();
    }

    @Test
    @Order(2)
    void testGetAvailableAssetForBuyPlacement_success() {
        StepVerifier.create         (exchangeService.getAvailableAssetForBuyPlacement(XzAsset.ZEUR, Asset.EUR))
                .consumeNextWith(availableAssetForBuyPlacement -> {
                    assertEquals(new BigDecimal("8.6600000000"), availableAssetForBuyPlacement);
                })
                .verifyComplete ();
    }

    @Test
    @Order(3)
    void testGetAvailableAssetForSellPlacement_success() {
        StepVerifier.create         (exchangeService.getAvailableAssetForSellPlacement(XzAsset.XLTC, Asset.LTC))
                .consumeNextWith(availableAssetForSellPlacement -> {
                    assertEquals(new BigDecimal("4.97857000"), availableAssetForSellPlacement);
                })
                .verifyComplete ();
    }

    @Test
    void testAddOrder_success() {
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
                        assertEquals(1, addOrderOutput.getTransactionIds().size() );
                        assertEquals(0, addOrderOutput.getErrors().size() );
                    })
                    .verifyComplete();
    }

    @Test
    void testOpenOrders_success() {
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
    void testGetTotalBuyPlacements_success() {
        StepVerifier.create         (exchangeService.getTotalBuyPlacements(Asset.EUR)        )
                    .consumeNextWith(totalBuyPlacements -> {
                        assertEquals(new BigDecimal("31.5900000000"), totalBuyPlacements);
                    })
                    .verifyComplete ();
    }

    @Test
    void testGetTotalSellPlacements_success() {
        StepVerifier.create         (exchangeService.getTotalSellPlacements(Asset.LTC))
                    .consumeNextWith(totalSellPlacements -> {
                        assertEquals(new BigDecimal("3.50143000"), totalSellPlacements);
                    })
                    .verifyComplete ();
    }

    @BeforeAll
    static void startWiremockServer() throws InterruptedException {
        new WireMockServer().start();
    }
}
