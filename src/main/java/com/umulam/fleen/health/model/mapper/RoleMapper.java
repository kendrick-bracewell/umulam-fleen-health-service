package com.umulam.fleen.health.model.mapper;

import com.umulam.fleen.health.model.domain.Role;
import com.umulam.fleen.health.model.view.RoleView;

import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class RoleMapper {

  private RoleMapper() {

  }

  public static RoleView toRoleView(@NotNull Role role) {
    return RoleView.builder()
            .id(role.getId())
            .title(role.getTitle())
            .code(role.getCode())
            .description(role.getDescription())
            .createdOn(role.getCreatedOn())
            .updatedOn(role.getUpdatedOn())
            .build();
  }

  public static List<RoleView> toRoleViews(List<Role> roles) {
    if (roles != null && !roles.isEmpty()) {
      return roles
              .stream()
              .map(RoleMapper::toRoleView)
              .collect(Collectors.toList());
    }
    return Collections.emptyList();
  }

}
