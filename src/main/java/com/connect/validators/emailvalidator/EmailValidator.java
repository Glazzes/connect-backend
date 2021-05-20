package com.connect.validators.emailvalidator;

import com.connect.services.UserService;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class EmailValidator implements
        ConstraintValidator<EmailMustNotBeRegistered, String> {

    private final UserService userService;

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        // If a given email is not registered already the validation must be true
        return !userService.verifyIfEmailIsAlreadyRegistered(s);
    }
}
