package ru.shekhovtsov.dto;


import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LimitDto {
    private Long clientId;
    private BigDecimal dailyLimit;
}