package fr.enix.exchanges.service;

import com.github.tomakehurst.wiremock.WireMockServer;
import fr.enix.common.exception.eapi.KrakenEapiInvalidKeyException;
import fr.enix.exchanges.model.business.AddOrderInput;
import fr.enix.kraken.AddOrderType;
import fr.enix.kraken.Asset;
import fr.enix.kraken.AssetPair;
import fr.enix.kraken.OrderType;
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
public class TestExchangeService {

    @Autowired
    private ExchangeService exchangeService;

    @Test
    @Order(0)
    public void testGetBalance_success() {
        StepVerifier.create         ( exchangeService.getBalance() )
                    .consumeNextWith( balanceResponse -> {

                        assertEquals( new BigDecimal("2.48"), balanceResponse.getResult().get( Asset.LITECOIN.getCode() ));
                        assertEquals( new BigDecimal("40.25"), balanceResponse.getResult().get( Asset.EURO.getCode() ));

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
        StepVerifier.create(exchangeService.addOrder    ( AddOrderInput.builder   ()
                                           .assetPair   (AssetPair.LITECOIN_TO_EURO)
                                           .addOrderType(AddOrderType.SELL)
                                           .orderType   (OrderType.LIMIT)
                                           .price       (new BigDecimal(40  ))
                                           .volume      (new BigDecimal(1   ))
                                           .build       ())
                    )
                    .consumeNextWith(addOrderOutput -> {
                        assertEquals("sell 1.00000000 LTCEUR @ limit 40.00", addOrderOutput.getDescription());
                        assertTrue(addOrderOutput.getTransactionIds().contains("OA6DK2-UK35J-NYDBAF"));
                        assertTrue(addOrderOutput.getTransactionIds().size() == 1 );
                        assertTrue( addOrderOutput.getErrors().size() == 0 );
                    })
                    .verifyComplete();
    }

    @BeforeAll
    public static void startWiremockServer() throws InterruptedException {
        new WireMockServer().start();
    }
}
