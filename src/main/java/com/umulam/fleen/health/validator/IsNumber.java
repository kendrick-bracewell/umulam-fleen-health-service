package com.umulam.fleen.health.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = IsNumberValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface IsNumber {
  String message() default "Invalid number";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
