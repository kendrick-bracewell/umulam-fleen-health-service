package com.umulam.fleen.health.validator;

import com.umulam.fleen.health.validator.impl.NoMoreThanOneSessionADayValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NoMoreThanOneSessionADayValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NoMoreThanOneSessionADay {

  String message() default "No booking of multiple sessions in a day";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
