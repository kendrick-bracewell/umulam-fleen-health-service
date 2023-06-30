package com.umulam.fleen.health.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PhoneNumberValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MobilePhoneNumber {

  String message() default "Phone number is invalid";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
