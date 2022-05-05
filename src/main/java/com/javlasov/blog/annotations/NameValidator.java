package com.javlasov.blog.annotations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NameValidator implements ConstraintValidator<Name, String> {

    @Override
    public void initialize(Name constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String name, ConstraintValidatorContext constraintValidatorContext) {
        String regExp = "[a-zA-Z ]+"; // only letters in name
        return (name.trim().length() >= 2 && (name.matches(regExp)));
    }

}
