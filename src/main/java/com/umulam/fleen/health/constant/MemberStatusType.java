package com.umulam.fleen.health.constant;

import lombok.Getter;

@Getter
public enum MemberStatusType {

  ACTIVE("Active"),
  INACTIVE("Inactive");

  private final String value;

  MemberStatusType(String value) {
    this.value = value;
  }
}
