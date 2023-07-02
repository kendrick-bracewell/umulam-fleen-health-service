package com.umulam.fleen.health.constant.base;

import lombok.Getter;

@Getter
public enum ProfileType {

  USER("User"),
  PROFESSIONAL("Professional"),
  BUSINESS("Business");

  private final String value;

  ProfileType(String value) {
    this.value = value;
  }
}
