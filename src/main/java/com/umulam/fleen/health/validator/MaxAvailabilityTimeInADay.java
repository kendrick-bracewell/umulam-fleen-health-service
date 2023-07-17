package com.umulam.fleen.health.validator;

import com.umulam.fleen.health.validator.impl.IsNumberValidator;
import com.umulam.fleen.health.validator.impl.MaxAvailabilityTimeInADayValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = MaxAvailabilityTimeInADayValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MaxAvailabilityTimeInADay {

  String message() default "Invalid number of working hours in a day";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
