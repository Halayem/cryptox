package fr.enix.exchanges.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.enix.common.utils.file.ApplicationFileUtils;
import fr.enix.exchanges.model.ws.response.OpenOrdersResponse;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import static org.mockito.Matchers.anyString;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@RunWith(SpringRunner.class)
class KrakenPrivateRepositoryImplTest {

    @MockBean
    private KrakenPrivateRepository krakenPrivateRepository;

    @Test
    void testGetTotalSellOpenOrdersWhenApplicationAssetPairIsLitecoin() throws IOException, URISyntaxException {
        Mockito
        .when(krakenPrivateRepository.getOpenOrders())
        .thenReturn(
            Mono.just(
                new ObjectMapper().readValue(
                    ApplicationFileUtils.getStringFileContentFromResources("kraken/orders/open-orders_litecoin-euro.json"),
                    OpenOrdersResponse.class
                )
            )
        );

        Mockito
        .when(krakenPrivateRepository.getTotalSellOpenOrders(anyString()))
        .thenCallRealMethod();

        StepVerifier
        .create(krakenPrivateRepository.getTotalSellOpenOrders("litecoin-euro"))
        .consumeNextWith(balance ->
            assertEquals( new BigDecimal("3.50143000"), balance))
        .verifyComplete();
    }

    // @TODO go to integration tests
    void testGetBalanceWhenApplicationAssetIsLitecoin() {
        StepVerifier
                .create(krakenPrivateRepository.getBalanceByApplicationAsset("litecoin"))
                .consumeNextWith(balance ->
                        assertEquals( new BigDecimal("8.48"), balance))
                .verifyComplete();
    }

    // @TODO go to integration tests
    void testGetBalanceWhenApplicationAssetIsEuro() {
        StepVerifier
                .create(krakenPrivateRepository.getBalanceByApplicationAsset("euro"))
                .consumeNextWith(balance ->
                        assertEquals( new BigDecimal("40.25"), balance))
                .verifyComplete();
    }
}
