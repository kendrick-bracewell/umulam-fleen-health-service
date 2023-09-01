package com.umulam.fleen.health.constant.authentication;

import lombok.Getter;

@Getter
public enum VerificationType {

  PHONE("PHONE"),
  EMAIL("Email");

  private final String value;

  VerificationType(String value) {
    this.value = value;
  }
}
