package com.umulam.fleen.health.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CountryExistsValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CountryExists {
  String message() default "Country does not exists";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
