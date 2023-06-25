package com.umulam.fleen.health.constant;

import lombok.Getter;

@Getter
public enum MaterialStatus {

  PENDING("Pending"),
  APPROVED("Approved"),
  DENIED("Denied");

  private final String value;

  MaterialStatus(String value) {
    this.value = value;
  }
}
