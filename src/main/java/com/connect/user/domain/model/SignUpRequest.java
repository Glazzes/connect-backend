package com.connect.user.domain.model;

import com.connect.shared.validator.emailvalidator.EmailMustNotBeRegistered;
import com.connect.shared.validator.usernamevalidator.UsernameMustNotBeRegistered;
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
