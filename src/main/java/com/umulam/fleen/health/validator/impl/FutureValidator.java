package com.umulam.fleen.health.validator.impl;

import com.umulam.fleen.health.validator.Future;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

import static com.umulam.fleen.health.util.DateFormatUtil.DATE;

@Slf4j
public class FutureValidator implements ConstraintValidator<Future, String> {

  public void initialize(Future constraintAnnotation) { }

  public boolean isValid(String date, ConstraintValidatorContext context) {
    if (Objects.nonNull(date)) {
      try {
        final DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DATE);
        LocalDate after = LocalDate.parse(date, dtf);
        return LocalDate.now().isBefore(after);
      } catch (DateTimeParseException ex) {
        log.error(ex.getMessage(), ex);
      }
    }
    return false;
  }
}
