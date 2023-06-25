package com.umulam.fleen.health.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EmailAddressExistsValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EmailAddressExists {
  String message() default "Email Address already exists";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
