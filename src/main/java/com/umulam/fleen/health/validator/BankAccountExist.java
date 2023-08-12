package com.umulam.fleen.health.validator;

import com.umulam.fleen.health.validator.impl.BankAccountExistValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = BankAccountExistValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface BankAccountExist {

  String message() default "Bank Account does not exists";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
