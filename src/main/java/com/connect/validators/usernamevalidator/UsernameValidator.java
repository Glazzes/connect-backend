package com.connect.validators.usernamevalidator;

import com.connect.services.UserService;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class UsernameValidator implements
        ConstraintValidator<UsernameMustNotBeRegistered, String> {

    private final UserService userService;

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        // If a given username is not registered already the validation must be true
        return !userService.verifyIfUsernameIsAlreadyRegistered(s);
    }
}
