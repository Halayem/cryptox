package fr.enix.common.utils.cryptography;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

@Slf4j
public class MacCryptographyUtils {

    private final Mac mac;

    public MacCryptographyUtils(byte[] key, String algorithm) throws NoSuchAlgorithmException, InvalidKeyException {
        mac = Mac.getInstance (algorithm);
        mac.init(new SecretKeySpec(key, algorithm));
        log.info("MAC cryptography object created using, key, having length {}, algorithm: {}", Arrays.toString(key).length(), algorithm);
    }

    public byte[] encrypt(byte[] message) {
        return mac.doFinal(message);
    }
}
