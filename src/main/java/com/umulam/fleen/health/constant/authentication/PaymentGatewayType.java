package com.umulam.fleen.health.constant.authentication;

import com.umulam.fleen.health.adapter.ExternalSystemType;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum PaymentGatewayType implements ExternalSystemType {

  PAYSTACK("Paystack"),
  FLUTTERWAVE("Flutterwave");

  private final String value;

  PaymentGatewayType(String value) {
    this.value = value;
  }

  @Override
  public String getValue() {
    return value;
  }
}
