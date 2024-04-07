package com.example.ElectronicStore.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UserNameValidatorImpl implements ConstraintValidator<UserNameValidator, String> {

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {

        if (s.isBlank())
            return false;
        else
            return true;
    }
}
