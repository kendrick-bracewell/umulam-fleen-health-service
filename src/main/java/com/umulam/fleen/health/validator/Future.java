package com.umulam.fleen.health.validator;

import com.umulam.fleen.health.validator.impl.FutureValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = FutureValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Future {
  String message() default "Date must be in the future";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}
