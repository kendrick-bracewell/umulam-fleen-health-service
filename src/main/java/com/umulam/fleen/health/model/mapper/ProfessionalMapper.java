package com.umulam.fleen.health.model.mapper;

import com.umulam.fleen.health.model.domain.Professional;
import com.umulam.fleen.health.model.view.ProfessionalView;
import org.springframework.lang.NonNull;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ProfessionalMapper {
  private ProfessionalMapper() {}

  public static ProfessionalView toProfessionalView(@NonNull Professional entry) {
    return ProfessionalView.builder()
            .id(entry.getId())
            .title(entry.getTitle())
            .yearsOfExperience(entry.getYearsOfExperience())
            .areaOfExpertise(entry.getAreaOfExpertise())
            .createdOn(entry.getCreatedOn())
            .updatedOn(entry.getUpdatedOn())
            .country(CountryMapper.toCountryView(entry.getCountry()))
            .member(MemberMapper.toMemberView(entry.getMember()))
            .build();
  }

  public static List<ProfessionalView> toProfessionalViews(List<Professional> entries) {
    if (Objects.nonNull(entries) && !entries.isEmpty()) {
      return entries
              .stream()
              .filter(Objects::nonNull)
              .map(ProfessionalMapper::toProfessionalView)
              .collect(Collectors.toList());
    }
    return Collections.emptyList();
  }
}
