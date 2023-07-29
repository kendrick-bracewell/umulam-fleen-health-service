package com.umulam.fleen.health.adapter.paystack.model.enums;

import com.umulam.fleen.health.adapter.ApiParameter;

public enum PsRecipientType implements ApiParameter {

  NUBAN("nuban"),
  MOBILE_MONEY("mobile_money"),
  BASA("basa");

  private final String value;

  PsRecipientType(String value) {
    this.value = value;
  }

  @Override
  public String getValue() {
    return value;
  }
}
