package com.example.springweb.validation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class ValidUsernameValidatorTest {

    private final ValidUsernameValidator validator = new ValidUsernameValidator();

    @ParameterizedTest
    @ValueSource(strings = {"abc", "john_doe", "User_123", "a1234567890123456789"})
    void acceptsValidUsernames(String username) {
        assertThat(validator.isValid(username, null)).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"ab", "this_username_is_definitely_too_long", "bad user", "has-dash", "spaces here", "!!!"})
    void rejectsInvalidUsernames(String username) {
        assertThat(validator.isValid(username, null)).isFalse();
    }

    @Test
    void rejectsNull() {
        assertThat(validator.isValid(null, null)).isFalse();
    }
}
