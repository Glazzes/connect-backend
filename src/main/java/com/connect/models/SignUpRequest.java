package com.connect.models;

import com.connect.validators.emailvalidator.EmailMustNotBeRegistered;
import com.connect.validators.usernamevalidator.UsernameMustNotBeRegistered;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class SignUpRequest {

    @UsernameMustNotBeRegistered
    @Size(min = 3, max = 40)
    @NotNull
    private String username;

    @Email
    @EmailMustNotBeRegistered
    @Size(min = 3, max = 100)
    @NotNull
    private String email;

    @Size(min = 8, max = 100)
    @NotNull
    private String password;

}
