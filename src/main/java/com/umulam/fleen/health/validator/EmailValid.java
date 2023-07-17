package com.umulam.fleen.health.validator;

import com.umulam.fleen.health.validator.impl.EmailValidValidator;
import com.umulam.fleen.health.validator.impl.IsNumberValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EmailValidValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EmailValid {

  String message() default "Invalid email";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
