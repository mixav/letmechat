package com.damnteam.letmechat.constraint;

import org.passay.*;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {

    @Override
    public void initialize(ValidPassword constraintAnnotation) {
    }


    @Override
    public boolean isValid(String userPassword, ConstraintValidatorContext context) {
        PasswordValidator passwordValidator =
                new PasswordValidator(Arrays.asList(
                        new LengthRule(8, 30),
                        new UppercaseCharacterRule(1),
                        new DigitCharacterRule(1),
                        new SpecialCharacterRule(1),
                        new NumericalSequenceRule(3, false),
                        new AlphabeticalSequenceRule(3, false),
                        new QwertySequenceRule(3, false),
                        new WhitespaceRule()
                ));

        RuleResult ruleResult = passwordValidator.validate(new PasswordData(userPassword));

        if (ruleResult.isValid()) {
            return true;
        } else {
            List<String> validatorMessages = passwordValidator.getMessages(ruleResult);

            String messageTemplate = validatorMessages.stream().collect(Collectors.joining(","));
            context.buildConstraintViolationWithTemplate(messageTemplate)
                    .addConstraintViolation()
                    .disableDefaultConstraintViolation();
            return false;
        }


    }
}
