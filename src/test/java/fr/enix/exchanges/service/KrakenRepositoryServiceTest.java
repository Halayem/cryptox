package fr.enix.exchanges.service;

import fr.enix.common.service.KrakenRepositoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class KrakenRepositoryServiceTest {

    @Autowired private KrakenRepositoryService krakenRepositoryService;

    @Test
    public void testGetNewNonce_success() {
        assertEquals(16, krakenRepositoryService.getNewNonce().length());
    }

    @Test
    public void testGetNewNonce_run10Times_shouldGet10differentNonce() {
        Set<String> nonces = new HashSet();
        for( int i = 0 ; i < 10 ; i++ ) {
            nonces.add(krakenRepositoryService.getNewNonce());
        }

        assertEquals(10, nonces.size());
    }
}
