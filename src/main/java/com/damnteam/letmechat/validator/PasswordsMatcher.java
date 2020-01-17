package com.damnteam.letmechat.validator;

import com.damnteam.letmechat.data.dto.RegistrationDTO;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordsMatcher implements ConstraintValidator<PasswordsMatch, RegistrationDTO> {

    @Override
    public void initialize(PasswordsMatch constraintAnnotation) {

    }

    @Override
    public boolean isValid(RegistrationDTO value, ConstraintValidatorContext context) {
        return value.getPassword().equals(value.getMatchingPassword());
    }
}
