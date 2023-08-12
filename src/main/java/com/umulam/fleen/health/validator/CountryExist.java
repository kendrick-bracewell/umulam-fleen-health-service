package com.umulam.fleen.health.validator;

import com.umulam.fleen.health.validator.impl.CountryExistValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CountryExistValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CountryExist {
  String message() default "Country does not exists";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
