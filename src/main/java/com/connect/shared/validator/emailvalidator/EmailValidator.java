package com.connect.shared.validator.emailvalidator;

import com.connect.user.application.service.PostgresUserService;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class EmailValidator implements
        ConstraintValidator<EmailMustNotBeRegistered, String> {

    private final PostgresUserService postgresUserService;

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        // If a given email is not registered already the validation must be true
        return !postgresUserService.existsByEmail(s);
    }
}
