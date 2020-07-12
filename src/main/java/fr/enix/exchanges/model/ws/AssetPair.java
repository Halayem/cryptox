package fr.enix.exchanges.model.ws;

import fr.enix.exchanges.model.parameters.Asset;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
@EqualsAndHashCode
public class AssetPair {
    private Asset from;
    private Asset to;
}
