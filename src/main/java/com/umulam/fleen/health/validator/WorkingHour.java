package com.umulam.fleen.health.validator;

import com.umulam.fleen.health.validator.impl.TimeValidValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = TimeValidValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface WorkingHour {

  String message() default "Working hour is invalid";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}
