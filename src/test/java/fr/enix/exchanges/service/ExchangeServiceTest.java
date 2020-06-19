package fr.enix.exchanges.service;

import com.github.tomakehurst.wiremock.WireMockServer;
import fr.enix.common.exception.eapi.KrakenEapiInvalidKeyException;
import fr.enix.exchanges.model.business.input.AddOrderInput;
import fr.enix.exchanges.model.business.output.OpenOrderOutput;
import fr.enix.exchanges.model.websocket.AssetPair;
import fr.enix.kraken.AddOrderType;
import fr.enix.kraken.Asset;
import fr.enix.kraken.OrderType;
import fr.enix.kraken.XzAsset;
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
                    .consumeNextWith(openOrderOutputs -> {
                        final OpenOrderOutput openOrderOutput = openOrderOutputs.get(0);
                        assertTrue  (openOrderOutputs.size() == 1);
                        assertEquals("O7AHQZ-MAJTT-NIAZWV", openOrderOutput.getTransactionId() );
                        assertEquals(new BigDecimal("43.00"),       openOrderOutput.getPrice());
                        assertEquals(new BigDecimal("3.50143000"),  openOrderOutput.getVolume());
                    })
                    .verifyComplete();
    }

    @BeforeAll
    public static void startWiremockServer() throws InterruptedException {
        new WireMockServer().start();
    }
}
