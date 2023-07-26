package com.umulam.fleen.health.model.mapper;

import com.umulam.fleen.health.model.domain.HealthSession;
import com.umulam.fleen.health.model.view.ProfessionalScheduleHealthSessionView;
import com.umulam.fleen.health.model.view.healthsession.HealthSessionView;
import com.umulam.fleen.health.model.view.healthsession.HealthSessionViewBasic;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;


public class HealthSessionMapper {

  private HealthSessionMapper() { }

  public static ProfessionalScheduleHealthSessionView toProfessionalScheduledHealthSessionView(HealthSession entry) {
    if (nonNull(entry)) {
      return ProfessionalScheduleHealthSessionView.builder()
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

  public static HealthSessionView toHealthSessionView(HealthSession entry) {
    if (Objects.nonNull(entry)) {
      return HealthSessionView.builder()
        .id(entry.getId())
        .status(entry.getStatus().name())
        .date(entry.getDate())
        .time(entry.getTime())
        .timezone(entry.getTimeZone())
        .reference(entry.getTimeZone())
        .meetingLink(entry.getMeetingUrl())
        .eventLink(entry.getEventLink())
        .createdOn(entry.getCreatedOn())
        .updatedOn(entry.getUpdatedOn())
        .comment(entry.getComment())
        .note(entry.getNote())
        .location(entry.getLocation().name())
        .patient(MemberMapper.toMemberViewBasic(entry.getPatient()))
        .professional(MemberMapper.toMemberViewBasic(entry.getProfessional()))
        .build();
    }
    return null;
  }

  public static HealthSessionViewBasic toHealthSessionViewBasic(HealthSession entry) {
    if (Objects.nonNull(entry)) {
      return HealthSessionViewBasic.builder()
        .id(entry.getId())
        .status(entry.getStatus().name())
        .date(entry.getDate())
        .time(entry.getTime())
        .timezone(entry.getTimeZone())
        .reference(entry.getReference())
        .location(entry.getLocation().name())
        .patient(MemberMapper.toMemberViewBasic(entry.getPatient()))
        .build();
    }
    return null;
  }

  public static HealthSessionViewBasic toHealthSessionViewBasicPatient(HealthSession entry) {
    if (Objects.nonNull(entry)) {
      return HealthSessionViewBasic.builder()
        .id(entry.getId())
        .status(entry.getStatus().name())
        .date(entry.getDate())
        .time(entry.getTime())
        .timezone(entry.getTimeZone())
        .reference(entry.getReference())
        .location(entry.getLocation().name())
        .build();
    }
    return null;
  }

  public static List<HealthSessionView> toHealthSessionViews(List<HealthSession> entries) {
    if (Objects.nonNull(entries) && !entries.isEmpty()) {
      return entries
              .stream()
              .filter(Objects::nonNull)
              .map(HealthSessionMapper::toHealthSessionView)
              .collect(Collectors.toList());
    }
    return Collections.emptyList();
  }

  public static List<HealthSessionViewBasic> toHealthSessionViewBasic(List<HealthSession> entries) {
    if (Objects.nonNull(entries) && !entries.isEmpty()) {
      return entries
              .stream()
              .filter(Objects::nonNull)
              .map(HealthSessionMapper::toHealthSessionViewBasic)
              .collect(Collectors.toList());
    }
    return Collections.emptyList();
  }

  public static List<HealthSessionViewBasic> toHealthSessionViewBasicPatient(List<HealthSession> entries) {
    if (Objects.nonNull(entries) && !entries.isEmpty()) {
      return entries
        .stream()
        .filter(Objects::nonNull)
        .map(HealthSessionMapper::toHealthSessionViewBasicPatient)
        .collect(Collectors.toList());
    }
    return Collections.emptyList();
  }
}
