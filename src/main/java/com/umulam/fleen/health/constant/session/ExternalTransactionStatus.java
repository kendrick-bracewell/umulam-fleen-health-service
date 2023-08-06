package com.umulam.fleen.health.constant.session;

import com.umulam.fleen.health.adapter.ApiParameter;

public enum ExternalTransactionStatus implements ApiParameter {

  SUCCESSFUL("Successful"),
  SUCCESS("Success");

  private final String value;

  ExternalTransactionStatus(String value) {
    this.value = value;
  }

  @Override
  public String getValue() {
    return value;
  }
}
