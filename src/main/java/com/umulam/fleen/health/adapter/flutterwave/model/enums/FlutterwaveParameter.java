package com.umulam.fleen.health.adapter.flutterwave.model.enums;

import com.umulam.fleen.health.adapter.ApiParameter;

public enum FlutterwaveParameter implements ApiParameter {

  TRANSACTION_REFERENCE("tx_ref");

  private final String value;

  FlutterwaveParameter(String value) {
    this.value = value;
  }

  @Override
  public String getValue() {
    return value;
  }
}
