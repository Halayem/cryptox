package fr.enix.common.service;

public interface KrakenRepositoryService {

    String getNewNonce();
    String getHmacDigest( String nonce, String postData, String uri);
}
