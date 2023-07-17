package com.umulam.fleen.health.validator;

import com.umulam.fleen.health.validator.impl.PhoneNumberValidator;
import com.umulam.fleen.health.validator.impl.ValidAvailabilityStartTimeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidAvailabilityStartTimeValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidAvailabilityStartTime {

  String message() default "Invalid availability start time";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
