package fr.enix.exchanges.repository.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.enix.common.utils.file.ApplicationFileUtils;
import fr.enix.exchanges.model.ws.response.OpenOrdersResponse;
import fr.enix.exchanges.repository.KrakenPrivateRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@RunWith(SpringRunner.class)
class KrakenPrivateRepositoryImplTest {

    @Autowired
    private KrakenPrivateRepositoryImpl krakenPrivateRepository;

    @Test
    void testGetTotalSellOpenOrdersWhenApplicationAssetPairIsLitecoin() throws IOException, URISyntaxException {
        StepVerifier
        .create         ( newMockKrakenRepository(MethodToSpy.GET_OPEN_ORDERS).getTotalSellOpenOrders("litecoin-euro") )
        .consumeNextWith( balance ->
            assertEquals( new BigDecimal("3.50143000"), balance))
        .verifyComplete();
    }

    @Test
    void testGetTotalBuyOpenOrdersWhenApplicationAssetPairIsLitecoin() throws IOException, URISyntaxException {
        StepVerifier
        .create         ( newMockKrakenRepository(MethodToSpy.GET_OPEN_ORDERS).getTotalBuyOpenOrders("litecoin-euro") )
        .consumeNextWith( balance ->
            assertEquals( new BigDecimal("31.5900000000"), balance))
        .verifyComplete();
    }

    @Test
    void testGetAvailableAssetForSellPlacementWhenApplicationAssetPairIsLitecoinEuro() throws IOException, URISyntaxException {
        StepVerifier
        .create         ( newMockKrakenRepository(MethodToSpy.GET_OPEN_ORDERS, MethodToSpy.GET_BALANCE)
                          .getAvailableAssetForSellPlacementByApplicationAssetPair("litecoin-euro") )
        .consumeNextWith( balance ->
            assertEquals( new BigDecimal("4.97857000"), balance))
        .verifyComplete();
    }

    @Test
    void testGetAvailableAssetForBuyPlacementWhenApplicationAssetPairIsLitecoinEuro() throws IOException, URISyntaxException {
        StepVerifier
        .create         ( newMockKrakenRepository(MethodToSpy.GET_OPEN_ORDERS, MethodToSpy.GET_BALANCE)
                          .getAvailableAssetForBuyPlacementByApplicationAssetPair("litecoin-euro") )
        .consumeNextWith( balance ->
                assertEquals( new BigDecimal("8.6600000000"), balance))
        .verifyComplete();
    }

    private KrakenPrivateRepositoryImpl newMockKrakenRepository(MethodToSpy ...methodsToSpy) throws IOException, URISyntaxException {
        KrakenPrivateRepositoryImpl krakenPrivateRepositorySpy = Mockito.spy(krakenPrivateRepository);

        if ( isGetOpenOrdersToSpy   (methodsToSpy) ) { setupKrakenRepositorySpyForGetOpenOrder  (krakenPrivateRepositorySpy); }
        if ( isGetBalanceToSpy      (methodsToSpy) ) { setupKrakenRepositorySpyForGetBalance    (krakenPrivateRepositorySpy); }

        return krakenPrivateRepositorySpy;
    }

    private void setupKrakenRepositorySpyForGetOpenOrder(KrakenPrivateRepositoryImpl krakenRepositorySpy) throws IOException, URISyntaxException {
        Mockito
        .when(krakenRepositorySpy.getOpenOrders())
        .thenReturn(Mono.just(
            new ObjectMapper().readValue(
                ApplicationFileUtils.getStringFileContentFromResources("kraken/orders/open-orders_litecoin-euro.json"),
                OpenOrdersResponse.class
            )
        ));
    }

    private void setupKrakenRepositorySpyForGetBalance(KrakenPrivateRepositoryImpl krakenRepositorySpy) {
        Mockito
        .when       ( krakenRepositorySpy.getBalanceByApplicationAsset("litecoin"))
        .thenReturn ( Mono.just( new BigDecimal("8.48") ) );

        Mockito
        .when       ( krakenRepositorySpy.getBalanceByApplicationAsset("euro"))
        .thenReturn ( Mono.just( new BigDecimal("40.25") ) );
    }

    private boolean isGetOpenOrdersToSpy(MethodToSpy ...methodsToSpy) {
        return  Arrays
                .stream     (methodsToSpy)
                .anyMatch   (methodToSpy -> methodToSpy.equals(MethodToSpy.GET_OPEN_ORDERS));
    }

    private boolean isGetBalanceToSpy(MethodToSpy ...methodsToSpy) {
        return  Arrays
                .stream     (methodsToSpy)
                .anyMatch   (methodToSpy -> methodToSpy.equals(MethodToSpy.GET_BALANCE));
    }
    private enum MethodToSpy {
        GET_OPEN_ORDERS, GET_BALANCE
    }

}
