package fr.enix.common.service.impl;

import fr.enix.common.service.EncryptionService;
import fr.enix.common.utils.cryptography.MacCryptographyUtils;
import fr.enix.common.utils.cryptography.MessageDigestUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;

@Slf4j
public class EncryptionServiceImpl implements EncryptionService {

    private final MessageDigestUtils    messageDigestUtilsSha256;
    private final MacCryptographyUtils  macCryptographyUtilsHmacSha512;
    private final NonceHelper           nonceHelper;

    public EncryptionServiceImpl(final MessageDigestUtils     messageDigestUtilsSha256,
                                 final MacCryptographyUtils   macCryptographyUtilsHmacSha512) {

        this.messageDigestUtilsSha256       = messageDigestUtilsSha256;
        this.macCryptographyUtilsHmacSha512 = macCryptographyUtilsHmacSha512;
        nonceHelper = new NonceHelper();
    }

    /**
     * Support generation of 899 different nonce for the same millisecond precision
     */
    @Override
    public String getNewNonce() {
        final String nonce = Long.toString(Instant.now().toEpochMilli())
                             + nonceHelper.getUnique3Digits();
        log.info("generating new nonce: {}", nonce);
        return nonce;
    }

    @Override
    public String getHmacDigest(String nonce, String postData, String uri) {
        return
            Base64.getEncoder().encodeToString( macCryptographyUtilsHmacSha512.encrypt(
                ArrayUtils.addAll(
                    uri.getBytes(StandardCharsets.UTF_8),
                    messageDigestUtilsSha256.getDigest(nonce + postData)
                )
            ));
    }

    private static class NonceHelper {
        private static final int MIN = 100;
        private int i = MIN;
        private synchronized int getUnique3Digits() {
            if ( ++i == 1000 ) { i = 100; }
            return i;
        }
    }

}
