package com.umulam.fleen.health.constant.authentication;

import lombok.Getter;

@Getter
public enum MfaSetupStatus {

  COMPLETE("Complete"),
  IN_PROGRESS("In progress"),
  NOT_SET("Not Set");

  private final String value;

  MfaSetupStatus(String value) {
    this.value = value;
  }
}
