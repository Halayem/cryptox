package fr.enix.mapper;

import fr.enix.exchanges.model.websocket.AssetPair;

public class AssetMapper {

    public String mapAssetPairForWebSocket(final fr.enix.exchanges.model.ws.AssetPair assetPair) {
        return assetPair.getFrom() + "/" + assetPair.getTo();
    }

    public String mapAssetPairForWebSocket(final AssetPair assetPair) {
        return assetPair.getFrom() + "/" + assetPair.getTo();
    }

    public String mapAssetPairForWebService(final AssetPair assetPair) {
        return assetPair.getFrom().toString() + assetPair.getTo().toString();
    }
}
