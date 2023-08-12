package com.umulam.fleen.health.adapter.banking.flutterwave.model.enums;

import com.umulam.fleen.health.adapter.ApiParameter;

public enum FlutterwaveParameter implements ApiParameter {

  TRANSACTION_REFERENCE("tx_ref"),
  SOURCE_CURRENCY("source_currency"),
  DESTINATION_CURRENCY("destination_currency"),
  AMOUNT("amount"),
  CURRENCY("currency"),
  TYPE("type");


  private final String value;

  FlutterwaveParameter(String value) {
    this.value = value;
  }

  @Override
  public String getValue() {
    return value;
  }
}
