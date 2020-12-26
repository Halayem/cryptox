package fr.enix.exchanges.service.impl;

import com.github.tomakehurst.wiremock.WireMockServer;
import fr.enix.exchanges.mapper.AddOrderMapper;
import fr.enix.exchanges.model.business.input.AddOrderInput;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

@SpringBootTest
@Slf4j
class KrakenExchangeServiceImplTest {

    @Autowired private ExchangeServiceImpl exchangeService;
    @Autowired private AddOrderMapper addOrderMapper;

    /**
     * The real exception is Exceptions.RetryExhaustedException, but it was static and not public class
     */
    @Test
    void testAddOrder_shouldThrowIllegalStateException() {
        StepVerifier
        .create     (newOrderInputForSellPlacementThatActivateAddOrderInvalidNonceStub().flatMap(exchangeService::addOrder))
        .expectError(IllegalStateException.class)
        .verify     ();
    }
    private Mono<AddOrderInput> newOrderInputForSellPlacementThatActivateAddOrderInvalidNonceStub() {
        return addOrderMapper.newAddOrderInputForSellPlacement("litecoin-euro", new BigDecimal("1"), new BigDecimal("40"));
    }
    // ------------------------------------------------
    // ------------ W I R E  M O C K ------------------
    // ------------------------------------------------
    private static final WireMockServer WIREMOCK_SERVER = new WireMockServer();

    @BeforeAll
    public static void setup() {
        log.info("starting wiremock server ...");
        WIREMOCK_SERVER.start();
        log.info("wiremock server started");
    }

    @AfterAll
    public static void tearDown() {
        log.info("stopping wiremock server ...");
        WIREMOCK_SERVER.stop();
        log.info("wiremock server stopped");
    }
}
