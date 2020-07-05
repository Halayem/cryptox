package fr.enix.common.service.impl;

import fr.enix.common.service.KrakenRepositoryService;
import fr.enix.common.utils.cryptography.MacCryptographyUtils;
import fr.enix.common.utils.cryptography.MessageDigestUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.nio.charset.Charset;
import java.time.Instant;
import java.util.Base64;

public class KrakenRepositoryServiceImpl implements KrakenRepositoryService {

    private final MessageDigestUtils    messageDigestUtilsSha256;
    private final MacCryptographyUtils  macCryptographyUtilsHmacSha512;
    private final NonceHelper           nonceHelper;

    public KrakenRepositoryServiceImpl(final MessageDigestUtils     messageDigestUtilsSha256,
                                       final MacCryptographyUtils   macCryptographyUtilsHmacSha512) {

        this.messageDigestUtilsSha256       = messageDigestUtilsSha256;
        this.macCryptographyUtilsHmacSha512 = macCryptographyUtilsHmacSha512;
        nonceHelper = new NonceHelper();
    }
    @Override
    public String getNewNonce() {
        return  Long.toString(Instant.now().toEpochMilli()) +
                nonceHelper.getUnique3Digits();
    }

    @Override
    public String getHmacDigest(String nonce, String postData, String uri) {
        return
            Base64.getEncoder().encodeToString( macCryptographyUtilsHmacSha512.encrypt(
                ArrayUtils.addAll(
                    uri.getBytes(Charset.forName("UTF-8")),
                    messageDigestUtilsSha256.getDigest(nonce + postData)
                )
            ));
    }

    private static class NonceHelper {
        private static final int MIN = 100;
        private static final int MAX = 1000;
        private int i = MIN;
        private synchronized int getUnique3Digits() {
            if ( ++i == 1000 ) { i = 100; }
            return i;
        }
    }

}
