package com.umulam.fleen.health.constant.authentication;

import lombok.Getter;

@Getter
public enum MfaType {

  SMS("SMS"),
  EMAIL("Email"),
  AUTHENTICATOR("Authenticator"),
  NONE("None");

  private final String value;

  MfaType(String value) {
    this.value = value;
  }
}
