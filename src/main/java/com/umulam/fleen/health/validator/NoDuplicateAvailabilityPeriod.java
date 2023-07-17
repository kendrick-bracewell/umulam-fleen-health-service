package com.umulam.fleen.health.validator;

import com.umulam.fleen.health.validator.impl.NoDuplicationAvailabilityPeriodValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NoDuplicationAvailabilityPeriodValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NoDuplicateAvailabilityPeriod {

  String message() default "No duplicate availability periods allowed";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
