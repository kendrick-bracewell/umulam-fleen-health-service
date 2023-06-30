package com.umulam.fleen.health.constant;

import lombok.Getter;

@Getter
public enum EmailMessageSource {

  NO_REPLY("noreply@fleenhealth.com"),
  SUPPORT("support@fleenhealth.com"),
  BASE("volunux@gmail.com");
  private final String value;

  EmailMessageSource(String value) {
    this.value = value;
  }
}
