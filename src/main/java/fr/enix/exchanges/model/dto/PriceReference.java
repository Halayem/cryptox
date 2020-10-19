package fr.enix.exchanges.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Getter
public class PriceReference implements Serializable {

    private final String applicationAssetPair;
    private final BigDecimal price;
    private final String updatedBy;
    private final LocalDateTime updatedAt;
}
