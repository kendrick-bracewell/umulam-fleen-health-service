package com.umulam.fleen.health.model.mapper;

import com.umulam.fleen.health.model.domain.ProfessionalAvailability;
import com.umulam.fleen.health.model.view.ProfessionalAvailabilityView;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ProfessionalAvailabilityMapper {

  private ProfessionalAvailabilityMapper() {}

  public static ProfessionalAvailabilityView toProfessionalAvailabilityView(ProfessionalAvailability entry) {
    if (Objects.nonNull(entry)) {
      return ProfessionalAvailabilityView.builder()
        .dayOfTheWeek(entry.getDayOfWeek().name())
        .startTime(entry.getStartTime())
        .endTime(entry.getEndTime())
        .build();
    }
    return null;
  }

  public static List<ProfessionalAvailabilityView> toProfessionalAvailabilityViews(List<ProfessionalAvailability> entries) {
    if (entries != null && !entries.isEmpty()) {
      return entries
              .stream()
              .filter(Objects::nonNull)
              .map(ProfessionalAvailabilityMapper::toProfessionalAvailabilityView)
              .collect(Collectors.toList());
    }
    return Collections.emptyList();
  }
}
