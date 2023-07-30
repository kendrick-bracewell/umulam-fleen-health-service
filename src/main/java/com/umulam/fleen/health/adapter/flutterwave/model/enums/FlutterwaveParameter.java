package com.umulam.fleen.health.adapter.flutterwave.model.enums;

import com.umulam.fleen.health.adapter.ApiParameter;

public enum FlutterwaveParameter implements ApiParameter {

  ACCOUNT_NUMBER("account_number"),
  BANK_CODE("account_bank");

  private final String value;

  FlutterwaveParameter(String value) {
    this.value = value;
  }

  @Override
  public String getValue() {
    return value;
  }
}
