package com.example.springweb.dto;

import java.math.BigDecimal;

import com.example.springweb.domain.AccountStatus;

/**
 * Payload for updating an account. Bean Validation constraints are added in the
 * validation feature.
 */
public record UpdateAccountRequest(
        String username,
        String email,
        String password,
        AccountStatus status,
        BigDecimal balance
) {
}
