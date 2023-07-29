package com.umulam.fleen.health.constant.session;

import com.umulam.fleen.health.adapter.ApiParameter;

public enum CurrencyType implements ApiParameter {

  NGN("NGN"),
  GHS("GHS"),
  ZAR("ZAR");

  private final String value;

  CurrencyType(String value) {
    this.value = value;
  }

  @Override
  public String getValue() {
    return value;
  }
}
