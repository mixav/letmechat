package com.damnteam.letmechat.data.dto;

import com.damnteam.letmechat.validator.PasswordsMatch;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@PasswordsMatch
public class RegistrationDTO {

    @NotNull
    @NotEmpty
    @Length(max = 10)
    private String login;

    @NotNull
    @NotEmpty
    @Length(min = 8)
    private String password;

    @NotNull
    @NotEmpty
    private String matchingPassword;

    @NotNull
    @NotEmpty
    private String firstName;

    @NotNull
    @NotEmpty
    private String lastName;
}
