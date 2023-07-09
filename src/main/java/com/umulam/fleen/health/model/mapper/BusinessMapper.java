package com.umulam.fleen.health.model.mapper;

import com.umulam.fleen.health.model.domain.Business;
import com.umulam.fleen.health.model.view.BusinessView;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class BusinessMapper {

  private BusinessMapper() {}

  public static BusinessView toBusinessView(Business entry) {
    if (Objects.nonNull(entry)) {
      return BusinessView.builder()
              .id(entry.getId())
              .name(entry.getName())
              .description(entry.getDescription())
              .city(entry.getCity())
              .contactAddress(entry.getContactAddress())
              .websiteLink(entry.getWebsiteLink())
              .createdOn(entry.getCreatedOn())
              .updatedOn(entry.getUpdatedOn())
              .registrationNumberOrId(entry.getRegistrationNumberOrId())
              .country(CountryMapper.toCountryView(entry.getCountry()))
              .member(MemberMapper.toMemberView(entry.getMember()))
              .build();
    }
    return null;
  }

  public static List<BusinessView> toBusinessViews(List<Business> entries) {
    if (Objects.nonNull(entries) && !entries.isEmpty()) {
      return entries
              .stream()
              .filter(Objects::nonNull)
              .map(BusinessMapper::toBusinessView)
              .collect(Collectors.toList());
    }
    return Collections.emptyList();
  }
}
