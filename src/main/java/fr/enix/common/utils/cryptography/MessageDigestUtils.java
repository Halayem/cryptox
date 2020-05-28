package fr.enix.common.utils.cryptography;

import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Slf4j
public class MessageDigestUtils {

    private MessageDigest messageDigest;

    public MessageDigestUtils(String algorithm) throws NoSuchAlgorithmException {
        messageDigest = MessageDigest.getInstance(algorithm);
        log.info("message digest cryptography object created using, algorithm: {}", algorithm);
    }

    public byte[] getDigest(String message) {
        return messageDigest.digest(message.getBytes(Charset.forName("UTF-8")));
    }
}
