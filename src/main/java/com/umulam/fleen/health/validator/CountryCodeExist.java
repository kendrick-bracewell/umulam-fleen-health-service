package com.umulam.fleen.health.validator;

import com.umulam.fleen.health.validator.impl.CountryCodeExistValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CountryCodeExistValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CountryCodeExist {
  String message() default "Country code already exists";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

}
