package com.umulam.fleen.health.constant.authentication;

import lombok.Getter;

@Getter
public enum TokenType {

  ACCESS_TOKEN("ACCESS_TOKEN"),
  REFRESH_TOKEN("REFRESH_TOKEN"),
  RESET_PASSWORD("RESET_PASSWORD");

  private final String value;

  TokenType(String value) {
    this.value = value;
  }

}
