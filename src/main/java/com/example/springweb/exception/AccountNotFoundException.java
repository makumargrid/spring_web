package com.example.springweb.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when an account cannot be located by id.
 *
 * <p>Annotated with {@code @ResponseStatus(NOT_FOUND)} so it maps to HTTP 404 even
 * before the global exception handler is introduced. Once the RFC 7807 handler
 * exists, that handler takes precedence and renders a problem+json body.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class AccountNotFoundException extends RuntimeException {

    public AccountNotFoundException(Long id) {
        super("Account not found with id: " + id);
    }
}
