package com.umulam.fleen.health.constant.session;

import com.umulam.fleen.health.adapter.ApiParameter;

public enum TransactionStatus implements ApiParameter {

  PENDING("Pending"),
  FAILED("Failed"),
  SUCCESS("Success"),
  REFUNDED("Refunded"),
  CANCELLED("Cancelled"),
  REVERSED("Reversed");

  private final String value;

  TransactionStatus(String value) {
    this.value = value;
  }

  @Override
  public String getValue() {
    return value;
  }
}
 
