package com.umulam.fleen.health.constant.authentication;

import lombok.Getter;

@Getter
public enum NextAuthentication {

  NONE("None"),
  PRE_VERIFICATION("Pre Verification"),
  MFA("MFA");

  private final String value;

  NextAuthentication(String value) {
    this.value = value;
  }
}
