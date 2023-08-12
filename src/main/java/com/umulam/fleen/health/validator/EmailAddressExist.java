package com.umulam.fleen.health.validator;

import com.umulam.fleen.health.validator.impl.EmailAddressExistValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EmailAddressExistValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EmailAddressExist {
  String message() default "Email Address already exists";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
