package com.umulam.fleen.health.validator;

import com.umulam.fleen.health.validator.impl.CountryCodeExistsValidator;
import com.umulam.fleen.health.validator.impl.VerificationMessageTemplateExistsValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = VerificationMessageTemplateExistsValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface VerificationMessageTemplateExists {
  String message() default "Verification Message Template does not exists";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

}