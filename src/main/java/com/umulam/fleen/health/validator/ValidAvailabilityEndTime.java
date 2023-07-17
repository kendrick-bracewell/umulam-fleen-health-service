package com.umulam.fleen.health.validator;

import com.umulam.fleen.health.validator.impl.PhoneNumberValidator;
import com.umulam.fleen.health.validator.impl.ValidAvailabilityEndTimeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidAvailabilityEndTimeValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidAvailabilityEndTime {

  String message() default "Invalid availability end time";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
