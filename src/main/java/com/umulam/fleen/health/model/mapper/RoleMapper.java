package com.umulam.fleen.health.model.mapper;

import com.umulam.fleen.health.model.domain.Role;
import com.umulam.fleen.health.model.view.RoleView;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class RoleMapper {

  private RoleMapper() {}

  public static RoleView toRoleView(Role entry) {
    if (Objects.nonNull(entry)) {
      return RoleView.builder()
              .id(entry.getId())
              .title(entry.getTitle())
              .code(entry.getCode())
              .description(entry.getDescription())
              .createdOn(entry.getCreatedOn())
              .updatedOn(entry.getUpdatedOn())
              .build();
    }
    return null;
  }

  public static List<RoleView> toRoleViews(List<Role> entries) {
    if (entries != null && !entries.isEmpty()) {
      return entries
              .stream()
              .filter(Objects::nonNull)
              .map(RoleMapper::toRoleView)
              .collect(Collectors.toList());
    }
    return Collections.emptyList();
  }

}
