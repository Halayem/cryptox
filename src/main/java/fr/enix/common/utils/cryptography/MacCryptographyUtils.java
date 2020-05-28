package fr.enix.common.utils.cryptography;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Slf4j
public class MacCryptographyUtils {

    private Mac mac;

    public MacCryptographyUtils(byte[] key, String algorithm) throws NoSuchAlgorithmException, InvalidKeyException {
        mac = Mac.getInstance   (algorithm);
        mac.init(new SecretKeySpec(key, algorithm));
        log.info("MAC cryptography object created using, key: {}, algorithm: {}", key.toString(), algorithm);
    }


    public byte[] encrypt(byte[] message) {
        return mac.doFinal(message);
    }
}
