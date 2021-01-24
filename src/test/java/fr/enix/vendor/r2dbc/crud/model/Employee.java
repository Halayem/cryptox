package fr.enix.vendor.r2dbc.crud.model;

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
public class Employee {

    @Id
    private Long id;
    private String name;
    private BigDecimal salary;
    private LocalDateTime hiringDate;
}
