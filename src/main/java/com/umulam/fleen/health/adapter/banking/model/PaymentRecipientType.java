package com.umulam.fleen.health.adapter.banking.model;

import com.umulam.fleen.health.adapter.ApiParameter;

public enum PaymentRecipientType implements ApiParameter {

  NUBAN("nuban"),
  MOBILE_MONEY("mobile_money"),
  BASA("basa"),
  FLUTTERWAVE("flutterwave");

  private final String value;

  PaymentRecipientType(String value) {
    this.value = value;
  }

  @Override
  public String getValue() {
    return value;
  }
}
