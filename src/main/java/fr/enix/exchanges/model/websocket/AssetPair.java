package fr.enix.exchanges.model.websocket;

import fr.enix.exchanges.model.parameters.XzAsset;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AssetPair {
    private XzAsset from;
    private XzAsset to;
}
