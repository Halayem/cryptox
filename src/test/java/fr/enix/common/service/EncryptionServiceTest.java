package fr.enix.common.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class EncryptionServiceTest {

    @Autowired private EncryptionService encryptionService;

    @Test
    void testGetNewNonce_success() {
        assertEquals(16, encryptionService.getNewNonce().length());
    }

    @Test
    void testGetNewNonce_shouldGenerate899DifferentNonce() {
        Set<String> nonces = new HashSet();
        for ( int i = 0 ; i < 899 ; i++ ) {
            nonces.add(encryptionService.getNewNonce());
        }

        assertEquals(899, nonces.size());
    }

}
