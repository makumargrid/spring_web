package com.example.springweb.dto;

import java.math.BigDecimal;

import com.example.springweb.domain.AccountStatus;

/**
 * Payload for creating an account. Bean Validation constraints are added in the
 * validation feature.
 */
public record CreateAccountRequest(
        String username,
        String email,
        String password,
        AccountStatus status,
        BigDecimal balance
) {
}
