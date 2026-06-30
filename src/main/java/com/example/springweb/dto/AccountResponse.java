package com.example.springweb.dto;

import java.math.BigDecimal;
import java.time.Instant;

import com.example.springweb.domain.AccountStatus;

/**
 * Account view returned to clients. Deliberately omits the password.
 */
public record AccountResponse(
        Long id,
        String username,
        String email,
        AccountStatus status,
        BigDecimal balance,
        Instant createdAt
) {
}
