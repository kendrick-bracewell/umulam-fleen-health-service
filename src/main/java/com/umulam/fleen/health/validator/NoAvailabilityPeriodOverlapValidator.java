package com.umulam.fleen.health.validator;

import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

import static com.umulam.fleen.health.model.dto.professional.UpdateProfessionalAvailabilityDto.AvailabilityPeriod;

@Slf4j
public class NoAvailabilityPeriodOverlapValidator implements ConstraintValidator<NoAvailabilityPeriodOverlap, List<AvailabilityPeriod>> {

  @Override
  public void initialize(NoAvailabilityPeriodOverlap constraintAnnotation) { }

  @Override
  public boolean isValid(List<AvailabilityPeriod> availabilityPeriods, ConstraintValidatorContext constraintValidatorContext) {
    return !hasOverlappingPeriods(availabilityPeriods);
  }

  private static boolean hasOverlappingPeriods(List<AvailabilityPeriod> periods) {
    int n = periods.size();
    for (int i = 0; i < n - 1; i++) {
      for (int j = i + 1; j < n; j++) {
        AvailabilityPeriod entry1 = periods.get(i);
        AvailabilityPeriod entry2 = periods.get(j);
        if (entry1.overlapsWith(entry2)) {
          return true;
        }
      }
    }
    return false;
  }
}
