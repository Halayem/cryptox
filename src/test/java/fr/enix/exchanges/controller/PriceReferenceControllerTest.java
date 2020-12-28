package fr.enix.exchanges.controller;

import fr.enix.common.configuration.CommonApplicationTestConfiguration;
import fr.enix.exchanges.model.dto.PriceReference;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(CommonApplicationTestConfiguration.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PriceReferenceControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private String httpHeadersForTest;

    @Test
    @Order(0)
    void testUpdatePriceReferenceForApplicationAssetPair() {
        webTestClient
        .post   ()
        .uri    ("/v1/price-references")
        .header("Authorization", httpHeadersForTest )
        .body   (
            Mono.just( PriceReference.builder().applicationAssetPair("litecoin-euro").price(new BigDecimal("45.52")).build()),
            PriceReference.class
        )
        .exchange()
        .expectStatus()
        .isOk();
    }

    @Test
    @Order(1)
    void testGetPriceReferenceByApplicationAssetPair() {
        webTestClient
        .get            ()
        .uri            ("/v1/price-references/{applicationAssetPair}", "litecoin-euro")
        .header         ("Authorization", httpHeadersForTest )
        .exchange       ()
        .expectStatus   ()
        .isOk           ()
        .expectBody     (PriceReference.class)
        .consumeWith    (response -> {
            assertNotNull(response.getResponseBody());

            final PriceReference priceReference = response.getResponseBody();

            assertEquals("litecoin-euro",       priceReference.getApplicationAssetPair() );
            assertEquals( new BigDecimal("45.52"),   priceReference.getPrice() );
            assertEquals( "user", priceReference.getUpdatedBy());
        });
    }
}
