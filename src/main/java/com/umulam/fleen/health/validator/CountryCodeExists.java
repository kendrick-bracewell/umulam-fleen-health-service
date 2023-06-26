package com.umulam.fleen.health.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CountryCodeExistsValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CountryCodeExists {
  String message() default "Country code already exists";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

}
