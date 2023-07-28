package com.umulam.fleen.health.adapter.paystack.model.enums;

import com.umulam.fleen.health.adapter.ApiParameter;

public enum PaystackParameter implements ApiParameter {

  ACCOUNT_NUMBER("account_number"),
  BANK_CODE("bank_code"),
  PER_PAGE("perPage"),
  CURRENCY("currency");

  private final String value;

  PaystackParameter(String value) {
    this.value = value;
  }

  @Override
  public String getValue() {
    return value;
  }
}
