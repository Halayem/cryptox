package fr.enix.common.service;

public interface EncryptionService {

    String getNewNonce();
    String getHmacDigest( String nonce, String postData, String uri);
}
