package com.umulam.fleen.health.constant;

import lombok.Getter;

@Getter
public enum RoleType {

  SUPER_ADMINISTRATOR("SuperAdministrator"),
  ADMINISTRATOR("Administrator"),
  PROFESSIONAL("Professional"),
  USER("User");

  private final String value;

  RoleType(String value) {
        this.value = value;
    }
}
