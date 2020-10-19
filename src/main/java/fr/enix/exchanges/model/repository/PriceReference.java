package fr.enix.exchanges.model.repository;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Getter
public class PriceReference {
    private String applicationAssetPair;
    private String updatedBy;
    private BigDecimal price;
    private LocalDateTime datetime;

    @Override
    public String toString() {
        return new  StringBuilder("price reference:").append("\n\t")
                    .append("- application asset pair -- <").append(applicationAssetPair    ).append(">").append("\n\t")
                    .append("- price ------------------- <").append(price                   ).append(">").append("\n\t")
                    .append("- updated by -------------- <").append(updatedBy               ).append(">").append("\n\t")
                    .append("- updated at -------------- <").append(datetime                ).append(">")
                    .toString();
    }
}
