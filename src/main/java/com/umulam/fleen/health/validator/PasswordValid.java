package com.umulam.fleen.health.validator;

import com.umulam.fleen.health.validator.impl.PasswordValidValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordValidValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordValid {
  String message() default "Password does not meet requirement";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
