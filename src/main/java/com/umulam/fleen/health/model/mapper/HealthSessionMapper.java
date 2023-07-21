package com.umulam.fleen.health.model.mapper;

import com.umulam.fleen.health.model.domain.HealthSession;
import com.umulam.fleen.health.model.view.ProfessionalScheduleHealthSessionView;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public class HealthSessionMapper {

  private HealthSessionMapper() { }

  public static ProfessionalScheduleHealthSessionView toProfessionalScheduledHealthSessionView(HealthSession entry) {
    if (Objects.nonNull(entry)) {
      ProfessionalScheduleHealthSessionView.builder()
        .date(entry.getDate())
        .time(entry.getTime())
        .timezone(entry.getTimeZone())
        .build();
    }
    return null;
  }

  public static List<ProfessionalScheduleHealthSessionView> toProfessionalScheduledHealthSessionViews(List<HealthSession> entries) {
    if (entries != null && !entries.isEmpty()) {
      return entries
              .stream()
              .filter(Objects::nonNull)
              .map(HealthSessionMapper::toProfessionalScheduledHealthSessionView)
              .collect(Collectors.toList());
    }
    return Collections.emptyList();
  }
}
