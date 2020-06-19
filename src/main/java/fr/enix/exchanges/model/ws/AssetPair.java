package fr.enix.exchanges.model.ws;

import fr.enix.kraken.Asset;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AssetPair {
    private Asset from;
    private Asset to;
}
