package com.example.ElectronicStore.validator;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = UserNameValidatorImpl.class)
public @interface UserNameValidator {

    String message() default "Invalid User Name!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};


}
