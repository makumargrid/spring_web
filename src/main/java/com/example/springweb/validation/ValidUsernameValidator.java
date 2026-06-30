package com.example.springweb.validation;

import java.util.regex.Pattern;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidUsernameValidator implements ConstraintValidator<ValidUsername, String> {

    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[A-Za-z0-9_]{3,20}$");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && USERNAME_PATTERN.matcher(value).matches();
    }
}
