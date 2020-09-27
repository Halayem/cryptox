package fr.enix.exchanges.model.repository;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Getter
@ToString
public class PriceReference {
    private BigDecimal price;
    private LocalDateTime datetime;
}
