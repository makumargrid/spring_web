package com.example.springweb.dto;

import java.math.BigDecimal;

import com.example.springweb.domain.AccountStatus;
import com.example.springweb.validation.ValidUsername;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

/**
 * Payload for creating an account.
 */
public record CreateAccountRequest(

        @ValidUsername
        String username,

        @NotBlank
        @Email
        String email,

        @NotBlank
        @Size(min = 8, max = 64, message = "password must be between 8 and 64 characters")
        String password,

        @NotNull
        AccountStatus status,

        @NotNull
        @PositiveOrZero
        BigDecimal balance
) {
}
