package com.umulam.fleen.health.model.mapper;

import com.umulam.fleen.health.model.domain.Country;
import com.umulam.fleen.health.model.view.CountryView;

import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CountryMapper {

  private CountryMapper() {}

  public static CountryView toCountryView(Country entry) {
    if (Objects.nonNull(entry)) {
      return CountryView.builder()
              .id(entry.getId())
              .title(entry.getTitle())
              .code(entry.getCode())
              .createdOn(entry.getCreatedOn())
              .updatedOn(entry.getUpdatedOn())
              .build();
    }
    return null;
  }

  public static List<CountryView> toCountryViews(List<Country> entries) {
    if (entries != null && !entries.isEmpty()) {
      return entries
              .stream()
              .filter(Objects::nonNull)
              .map(CountryMapper::toCountryView)
              .collect(Collectors.toList());
    }
    return Collections.emptyList();
  }
}
