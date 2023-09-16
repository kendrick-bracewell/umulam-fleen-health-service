package com.umulam.fleen.health.model.mapper;

import com.umulam.fleen.health.model.domain.Professional;
import com.umulam.fleen.health.model.view.professional.ProfessionalView;
import com.umulam.fleen.health.model.view.professional.ProfessionalViewBasic;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

public class ProfessionalMapper {
  private ProfessionalMapper() {}

  public static ProfessionalView toProfessionalView(Professional entry) {
    if (nonNull(entry)) {
      return ProfessionalView.builder()
              .id(entry.getId())
              .title(entry.getTitle().getValue())
              .yearsOfExperience(entry.getYearsOfExperience())
              .areaOfExpertise(entry.getAreaOfExpertise())
              .createdOn(entry.getCreatedOn())
              .updatedOn(entry.getUpdatedOn())
              .country(CountryMapper.toCountryView(entry.getCountry()))
              .member(MemberMapper.toMemberView(entry.getMember()))
              .professionalType(entry.getProfessionalType().getValue())
              .qualification(entry.getQualificationType().getValue())
              .availabilityStatus(entry.getAvailabilityStatus().name())
              .languagesSpoken(entry.getLanguagesSpoken())
              .price(entry.getPrice())
              .build();
    }
    return null;
  }

  public static ProfessionalViewBasic toProfessionalViewBasic(Professional entry) {
    if (nonNull(entry)) {
      return ProfessionalViewBasic.builder()
              .id(entry.getId())
              .title(entry.getTitle().getValue())
              .yearsOfExperience(entry.getYearsOfExperience())
              .areaOfExpertise(entry.getAreaOfExpertise())
              .country(CountryMapper.toCountryViewBasic(entry.getCountry()))
              .member(MemberMapper.toMemberViewBasic(entry.getMember()))
              .professionalType(entry.getProfessionalType().getValue())
              .qualification(entry.getQualificationType().getValue())
              .languagesSpoken(entry.getLanguagesSpoken())
              .price(entry.getPrice())
              .build();
    }
    return null;
  }

  public static List<ProfessionalView> toProfessionalViews(List<Professional> entries) {
    if (nonNull(entries) && !entries.isEmpty()) {
      return entries
              .stream()
              .filter(Objects::nonNull)
              .map(ProfessionalMapper::toProfessionalView)
              .collect(Collectors.toList());
    }
    return Collections.emptyList();
  }

  public static List<ProfessionalViewBasic> toProfessionalViewsBasic(List<Professional> entries) {
    if (nonNull(entries) && !entries.isEmpty()) {
      return entries
              .stream()
              .filter(Objects::nonNull)
              .map(ProfessionalMapper::toProfessionalViewBasic)
              .collect(Collectors.toList());
    }
    return Collections.emptyList();
  }
}
