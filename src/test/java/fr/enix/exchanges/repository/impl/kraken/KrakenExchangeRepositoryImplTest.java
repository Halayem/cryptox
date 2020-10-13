package fr.enix.exchanges.repository.impl.kraken;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.enix.common.utils.file.ApplicationFileUtils;
import fr.enix.exchanges.model.ws.response.OpenOrdersResponse;
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
class KrakenExchangeRepositoryImplTest {

    @Autowired
    private KrakenExchangeRepositoryImpl krakenExchangeRepository;

    @Test
    void testGetTotalSellOpenOrdersWhenApplicationAssetPairIsLitecoin() throws IOException, URISyntaxException {
        StepVerifier
        .create         ( newMockKrakenExchangeRepository(MethodToSpy.GET_OPEN_ORDERS).getTotalSellOpenOrders("litecoin-euro") )
        .consumeNextWith( balance ->
            assertEquals( new BigDecimal("3.50143000"), balance))
        .verifyComplete();
    }

    @Test
    void testGetTotalBuyOpenOrdersWhenApplicationAssetPairIsLitecoin() throws IOException, URISyntaxException {
        StepVerifier
        .create         ( newMockKrakenExchangeRepository(MethodToSpy.GET_OPEN_ORDERS).getTotalBuyOpenOrders("litecoin-euro") )
        .consumeNextWith( balance ->
            assertEquals( new BigDecimal("31.5900000000"), balance))
        .verifyComplete();
    }

    @Test
    void testGetAvailableAssetForSellPlacementWhenApplicationAssetPairIsLitecoinEuro() throws IOException, URISyntaxException {
        StepVerifier
        .create         ( newMockKrakenExchangeRepository(MethodToSpy.GET_OPEN_ORDERS, MethodToSpy.GET_BALANCE)
                          .getAvailableAssetForSellPlacementByApplicationAssetPair("litecoin-euro") )
        .consumeNextWith( balance ->
            assertEquals( new BigDecimal("4.97857000"), balance))
        .verifyComplete();
    }

    @Test
    void testGetAvailableAssetForBuyPlacementWhenApplicationAssetPairIsLitecoinEuro() throws IOException, URISyntaxException {
        StepVerifier
        .create         ( newMockKrakenExchangeRepository(MethodToSpy.GET_OPEN_ORDERS, MethodToSpy.GET_BALANCE)
                          .getAvailableAssetForBuyPlacementByApplicationAssetPair("litecoin-euro") )
        .consumeNextWith( balance ->
                assertEquals( new BigDecimal("8.6600000000"), balance))
        .verifyComplete();
    }

    private KrakenExchangeRepositoryImpl newMockKrakenExchangeRepository(MethodToSpy ...methodsToSpy) throws IOException, URISyntaxException {
        KrakenExchangeRepositoryImpl krakenExchangeRepositorySpy = Mockito.spy(krakenExchangeRepository);

        if ( isGetOpenOrdersToSpy   (methodsToSpy) ) { setupKrakenExchangeRepositorySpyForGetOpenOrder  (krakenExchangeRepositorySpy); }
        if ( isGetBalanceToSpy      (methodsToSpy) ) { setupKrakenExchangeRepositorySpyForGetBalance    (krakenExchangeRepositorySpy); }

        return krakenExchangeRepositorySpy;
    }

    private void setupKrakenExchangeRepositorySpyForGetOpenOrder(KrakenExchangeRepositoryImpl krakenExchangeRepositorySpy) throws IOException, URISyntaxException {
        Mockito
        .when(krakenExchangeRepositorySpy.getOpenOrders())
        .thenReturn(Mono.just(
            new ObjectMapper().readValue(
                ApplicationFileUtils.getStringFileContentFromResources("kraken/orders/open-orders_litecoin-euro.json"),
                OpenOrdersResponse.class
            )
        ));
    }

    private void setupKrakenExchangeRepositorySpyForGetBalance(KrakenExchangeRepositoryImpl krakenExchangeRepositorySpy) {
        Mockito
        .when       ( krakenExchangeRepositorySpy.getBalanceByApplicationAsset("litecoin"))
        .thenReturn ( Mono.just( new BigDecimal("8.48") ) );

        Mockito
        .when       ( krakenExchangeRepositorySpy.getBalanceByApplicationAsset("euro"))
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
