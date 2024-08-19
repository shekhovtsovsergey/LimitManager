package ru.shekhovtsov.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.shekhovtsov.dto.LimitDto;
import ru.shekhovtsov.service.LimitService;

import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/limits")
public class LimitController {

    private final LimitService limitService;

    @GetMapping("/{clientId}")
    public ResponseEntity<LimitDto> getLimit(@PathVariable Long clientId) {
        try {
            LimitDto limitDto = limitService.getLimit(clientId);
            return ResponseEntity.ok(limitDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping
    public ResponseEntity<LimitDto> createLimit(@RequestBody LimitDto limitDto) {
        LimitDto createdLimit = limitService.createLimit(limitDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdLimit);
    }

    @PutMapping("/{clientId}")
    public ResponseEntity<LimitDto> updateLimit(@PathVariable Long clientId, @RequestBody LimitDto limitDto) {
        LimitDto updatedLimit = limitService.updateLimit(clientId, limitDto);
        return ResponseEntity.ok(updatedLimit);
    }

    @DeleteMapping("/{clientId}")
    public ResponseEntity<Void> deleteLimit(@PathVariable Long clientId) {
        limitService.deleteLimit(clientId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{clientId}/reduce")
    public ResponseEntity<LimitDto> reduceLimit(@PathVariable Long clientId, @RequestParam BigDecimal amount) {
        LimitDto updatedLimit = limitService.reduceLimit(clientId, amount);
        return ResponseEntity.ok(updatedLimit);
    }

    @PostMapping("/{clientId}/restore")
    public ResponseEntity<LimitDto> restoreLimit(@PathVariable Long clientId, @RequestParam BigDecimal amount) {
        LimitDto updatedLimit = limitService.restoreLimit(clientId, amount);
        return ResponseEntity.ok(updatedLimit);
    }
}