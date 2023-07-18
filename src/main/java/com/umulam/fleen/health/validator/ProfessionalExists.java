package com.umulam.fleen.health.validator;

import com.umulam.fleen.health.validator.impl.ProfessionalExistsValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ProfessionalExistsValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ProfessionalExists {

  String message() default "Professional does not exists";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
