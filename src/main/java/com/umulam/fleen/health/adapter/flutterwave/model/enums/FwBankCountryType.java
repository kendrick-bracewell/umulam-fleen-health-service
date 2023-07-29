package com.umulam.fleen.health.adapter.flutterwave.model.enums;

import com.umulam.fleen.health.adapter.ApiParameter;

public enum FwBankCountryType implements ApiParameter {

  NG("NG"),
  GH("GH"),
  ZA("ZA");

  private final String value;

  FwBankCountryType(String value) {
    this.value = value;
  }

  @Override
  public String getValue() {
    return value;
  }
}
