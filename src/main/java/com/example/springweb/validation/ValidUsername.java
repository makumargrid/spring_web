package com.example.springweb.validation;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * Validates that a username is 3-20 characters long and contains only letters,
 * digits or underscores. Rejects {@code null} and blank values.
 */
@Documented
@Constraint(validatedBy = ValidUsernameValidator.class)
@Target({FIELD, PARAMETER, ANNOTATION_TYPE})
@Retention(RUNTIME)
public @interface ValidUsername {

    String message() default "username must be 3-20 characters and contain only letters, digits or underscores";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
