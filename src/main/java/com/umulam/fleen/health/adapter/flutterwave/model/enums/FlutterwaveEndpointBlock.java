package com.umulam.fleen.health.adapter.flutterwave.model.enums;

import com.umulam.fleen.health.adapter.EndpointBlock;

public enum FlutterwaveEndpointBlock implements EndpointBlock {

  BANKS("/banks"),
  ACCOUNTS("/accounts"),
  RESOLVE("/resolve");

  private final String value;

  FlutterwaveEndpointBlock(String value) {
    this.value = value;
  }

  @Override
  public String getValue() {
    return value;
  }
}
