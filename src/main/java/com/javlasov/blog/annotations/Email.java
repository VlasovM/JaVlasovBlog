package com.javlasov.blog.annotations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EmailValidator.class)
@Target(value = ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Email {

    String message() default "Неверный формат введённого e-mail";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
