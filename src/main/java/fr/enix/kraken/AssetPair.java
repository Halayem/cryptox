package fr.enix.kraken;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AssetPair {
    LITECOIN_TO_EURO(  Asset.LITECOIN.getCode()   + Asset.EURO.getCode(),
                     Asset.LITECOIN.getWsCode() + "/" + Asset.EURO.getWsCode());

    private String code;
    private String wsCode;
}
