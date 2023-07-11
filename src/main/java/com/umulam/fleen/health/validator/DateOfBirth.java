package com.umulam.fleen.health.validator;

import com.umulam.fleen.health.validator.impl.DateOfBirthValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DateOfBirthValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DateOfBirth {
  String message() default "Date of Birth is invalid";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}
