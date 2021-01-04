package fr.enix.exchanges.model.repository;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class Ticker {

    @Id
    private Long id;
    private String market;
    private String assetPair;
    private BigDecimal price;
    private LocalDateTime at;

}
