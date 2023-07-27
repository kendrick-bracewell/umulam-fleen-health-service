package com.umulam.fleen.health.validator;

import com.umulam.fleen.health.validator.impl.EnumValidValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = EnumValidValidator.class)
public @interface EnumOrdinalValid {

  Class<? extends Enum<?>> enumClass();
  String message() default "must be any of enum {enumClass}";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}
