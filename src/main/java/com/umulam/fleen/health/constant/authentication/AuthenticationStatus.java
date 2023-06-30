package com.umulam.fleen.health.constant.authentication;

import lombok.Getter;

@Getter
public enum AuthenticationStatus {

  IN_PROGRESS("In Progress"),
  COMPLETED("Completed");

  private final String value;

  AuthenticationStatus(String value) {
    this.value = value;
  }
}
