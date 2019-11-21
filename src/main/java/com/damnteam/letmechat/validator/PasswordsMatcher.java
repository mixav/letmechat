package com.damnteam.letmechat.validator;

import com.damnteam.letmechat.data.UserDTO;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordsMatcher implements ConstraintValidator<PasswordsMatch, UserDTO> {

    @Override
    public void initialize(PasswordsMatch constraintAnnotation) {

    }

    @Override
    public boolean isValid(UserDTO value, ConstraintValidatorContext context) {
        return value.getPassword().equals(value.getMatchingPassword());
    }
}
