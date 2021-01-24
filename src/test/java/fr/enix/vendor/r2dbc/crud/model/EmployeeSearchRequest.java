package fr.enix.vendor.r2dbc.crud.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class EmployeeSearchRequest {

    private BigDecimal salary;
    private LocalDateTime hiringDate;
}
