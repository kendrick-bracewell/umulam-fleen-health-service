package com.umulam.fleen.health.validator;

import com.umulam.fleen.health.validator.impl.ProfessionalValidValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ProfessionalValidValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ProfessionalValid {

  String message() default "Professional is not valid";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
