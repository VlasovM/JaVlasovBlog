package com.javlasov.blog.annotations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailValidator implements ConstraintValidator<Email, String> {

    @Override
    public void initialize(Email constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String emailUser, ConstraintValidatorContext constraintValidatorContext) {
        String validateEmail = "^[A-Za-z0-9]+(.)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]{2,3})$";
        return emailUser.matches(validateEmail);
    }

}
