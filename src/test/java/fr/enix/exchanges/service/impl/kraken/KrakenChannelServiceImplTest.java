package fr.enix.exchanges.service.impl.kraken;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.enix.common.utils.file.ApplicationFileUtils;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class KrakenChannelServiceImplTest {

    @Autowired private KrakenChannelServiceImpl krakenChannelService;

    @Test
    @Order(0)
    void testSaveChannelSubscriptionTickerForLitecoinEuroAssetPairHavingInvalidJsonRepresentation_shouldThrowException() {
        assertThrows(
            JsonProcessingException.class,
            () -> krakenChannelService.saveChannel(
                    ApplicationFileUtils.getStringFileContentFromResources(
                            "kraken/subscription/ticker/ltc_eur.invalid.json"
                    )
            )
        );
    }

    @Test
    @Order(1)
    void testSaveChannelSubscriptionTickerForLitecoinEuroAssetPairHavingValidJsonRepresentation_shouldNotThrowException(){
        assertDoesNotThrow( () ->
                krakenChannelService.saveChannel(
                        ApplicationFileUtils.getStringFileContentFromResources(
                                "kraken/subscription/ticker/ltc_eur.valid.json"
                        )
                )
        );
    }

    @Test
    @Order(2)
    void testSaveChannelSubscriptionTickerForRippleEuroAssetPairHavingValidJsonRepresentation_shouldNotThrowException(){
        assertDoesNotThrow( () ->
                krakenChannelService.saveChannel(
                        ApplicationFileUtils.getStringFileContentFromResources(
                                "kraken/subscription/ticker/xrp_eur.valid.json"
                        )
                )
        );
    }

    @Test
    @Order(3)
    void testGetApplicationAssetPairByChannelId_shouldReturnLitecoinEuroAsApplicationAssetPair() {
        StepVerifier.create(
                krakenChannelService.getApplicationAssetPairByChannelId(379l)
        ).consumeNextWith(applicationAssetPair ->
                assertEquals("litecoin-euro", applicationAssetPair)
        ).verifyComplete();
    }

    @Test
    @Order(4)
    void testGetApplicationAssetPairByChannelId_shouldReturnRippleEuroAsApplicationAssetPair() {
        StepVerifier.create(
                krakenChannelService.getApplicationAssetPairByChannelId(829l)
        ).consumeNextWith(applicationAssetPair ->
                assertEquals("ripple-euro", applicationAssetPair)
        ).verifyComplete();
    }

}
