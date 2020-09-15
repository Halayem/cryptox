package fr.enix.exchanges.service;

import fr.enix.common.service.KrakenRepositoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class KrakenRepositoryServiceTest {

    @Autowired private KrakenRepositoryService krakenRepositoryService;

    @Test
    void testGetNewNonce_success() {
        assertEquals(16, krakenRepositoryService.getNewNonce().length());
    }

    @Test
    void testGetNewNonce_shouldGenerate899DifferentNonce() {
        Set<String> nonces = new HashSet();
        for ( int i = 0 ; i < 899 ; i++ ) {
            nonces.add(krakenRepositoryService.getNewNonce());
        }

        assertEquals(899, nonces.size());
    }

}
