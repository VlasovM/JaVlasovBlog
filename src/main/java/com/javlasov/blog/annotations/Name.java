package com.javlasov.blog.annotations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NameValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Name {

    String message() default "Имя должно содержать только буквы и состоять минимум из 2 символов";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
