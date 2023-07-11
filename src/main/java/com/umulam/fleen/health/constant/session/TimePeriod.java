package com.umulam.fleen.health.constant.session;

import lombok.Getter;

@Getter
public enum TimePeriod {

  AM("Ante Meridiem"),
  PM("Post Meridiem"),
  NONE("None");

  private final String value;

  TimePeriod(String value) {
    this.value = value;
  }
}
