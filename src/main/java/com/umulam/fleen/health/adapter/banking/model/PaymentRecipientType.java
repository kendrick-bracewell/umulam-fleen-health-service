package com.umulam.fleen.health.adapter.banking.model;

import com.umulam.fleen.health.adapter.ApiParameter;

public enum PaymentRecipientType implements ApiParameter {

  NUBAN("nuban", "account"),
  MOBILE_MONEY("mobile_money", "mobilemoney"),
  BASA("basa", "basa");

  private final String value;
  private final String otherName;

  PaymentRecipientType(String value, String otherName) {
    this.value = value;
    this.otherName = otherName;
  }

  @Override
  public String getValue() {
    return value;
  }

  public String getOtherName() {
    return otherName;
  }
}
