package com.umulam.fleen.health.constant.authentication;

import com.umulam.fleen.health.adapter.ExternalSystemType;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum PaystackType implements ExternalSystemType {

  PAYSTACK("Paystack");

  private final String value;

  PaystackType(String value) {
    this.value = value;
  }

  @Override
  public String getValue() {
    return value;
  }
}
