package com.connect.shared.validator.usernamevalidator;

import com.connect.user.application.service.PostgresUserService;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class UsernameValidator implements
        ConstraintValidator<UsernameMustNotBeRegistered, String> {

    private final PostgresUserService postgresUserService;

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        // If a given username is not registered already the validation must be true
        return !postgresUserService.existsByUsername(s);
    }
}
