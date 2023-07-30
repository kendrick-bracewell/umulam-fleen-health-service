package com.umulam.fleen.health.validator.impl;

import com.umulam.fleen.health.validator.NoMoreThanOneSessionADay;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.umulam.fleen.health.model.dto.healthsession.BookHealthSessionDto.SessionPeriod;
import static com.umulam.fleen.health.util.DateTimeUtil.toDate;

public class NoMoreThanOneSessionADayValidator implements ConstraintValidator<NoMoreThanOneSessionADay, List<SessionPeriod>> {

  @Override
  public void initialize(NoMoreThanOneSessionADay constraintAnnotation) { }

  @Override
  public boolean isValid(List<SessionPeriod> sessionPeriods, ConstraintValidatorContext constraintValidatorContext) {
    return hasNoDuplicatesOrSameDay(sessionPeriods);
  }

  public static boolean hasNoDuplicatesOrSameDay(List<SessionPeriod> periods) {
    Set<LocalDate> dateSet = new HashSet<>();
    for (SessionPeriod period : periods) {
      LocalDate date = toDate(period.getDate());
      if (!dateSet.add(date)) {
        return false;
      }
    }
    return true;
  }

}
