package com.umulam.fleen.health.validator;

import com.umulam.fleen.health.validator.impl.PhoneNumberExistsValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PhoneNumberExistsValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PhoneNumberExists {
  String message() default "Phone Number already exists";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
