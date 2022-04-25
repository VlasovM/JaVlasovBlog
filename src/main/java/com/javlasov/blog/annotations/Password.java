package com.javlasov.blog.annotations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Password {
    String message() default "Пароль должен содержать буквы и цифры и быть не короче 7 символов";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
