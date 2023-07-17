package com.umulam.fleen.health.validator;

import com.umulam.fleen.health.validator.impl.AfterValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AfterValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface After {
  String message() default "Date must be in the future";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}
