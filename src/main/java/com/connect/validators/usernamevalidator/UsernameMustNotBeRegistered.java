package com.connect.validators.usernamevalidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {UsernameValidator.class})
public @interface UsernameMustNotBeRegistered {
    String message() default "This username is taken by another user.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
