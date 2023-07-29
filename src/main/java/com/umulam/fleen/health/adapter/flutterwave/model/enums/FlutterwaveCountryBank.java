package com.umulam.fleen.health.adapter.flutterwave.model.enums;

import com.umulam.fleen.health.adapter.ApiParameter;

public enum FlutterwaveCountryBank implements ApiParameter {

  NG("NG"),
  GH("GH"),
  ZA("ZA");

  private final String value;

  FlutterwaveCountryBank(String value) {
    this.value = value;
  }

  @Override
  public String getValue() {
    return value;
  }
}
