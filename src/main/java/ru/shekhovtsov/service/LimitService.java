package ru.shekhovtsov.service;

import ru.shekhovtsov.dto.LimitDto;

import java.math.BigDecimal;

public interface LimitService {
    LimitDto getLimit(Long clientId);
    LimitDto reduceLimit(Long clientId, BigDecimal amount);
    LimitDto restoreLimit(Long clientId, BigDecimal amount);
    void resetLimits();
    LimitDto createLimit(LimitDto limitDto);
    LimitDto updateLimit(Long clientId, LimitDto limitDto);
    void deleteLimit(Long clientId);
}
