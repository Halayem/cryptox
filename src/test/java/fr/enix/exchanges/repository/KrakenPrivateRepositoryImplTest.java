package fr.enix.exchanges.repository;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class KrakenPrivateRepositoryImplTest {

    @Autowired
    private KrakenPrivateRepository krakenPrivateRepository;

    @BeforeAll
    static void startWiremockServer() throws InterruptedException {
        new WireMockServer().start();
    }

    //@Test
    void testGetBalanceWhenApplicationAssetIsLitecoin() {
        StepVerifier
        .create(krakenPrivateRepository.getBalanceByApplicationAsset("litecoin"))
        .consumeNextWith(balance ->
            assertEquals( new BigDecimal("8.48"), balance))
        .verifyComplete();
    }

    @Test
    void testGetBalanceWhenApplicationAssetIsEuro() {
        StepVerifier
        .create(krakenPrivateRepository.getBalanceByApplicationAsset("euro"))
        .consumeNextWith(balance ->
            assertEquals( new BigDecimal("40.25"), balance))
        .verifyComplete();
    }
}
