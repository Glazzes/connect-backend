package com.connect.validators.emailvalidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {EmailValidator.class})
public @interface EmailMustNotBeRegistered {
    String message() default "This email is already taken by another user.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
