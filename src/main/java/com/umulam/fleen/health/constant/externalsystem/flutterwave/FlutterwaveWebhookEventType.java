package com.umulam.fleen.health.constant.externalsystem.flutterwave;

import com.umulam.fleen.health.adapter.ApiParameter;

public enum FlutterwaveWebhookEventType implements ApiParameter {

  CHARGE_COMPLETED("charge.completed"),
  TRANSFER_COMPLETED("transfer.completed");
  
  private final String value;

  FlutterwaveWebhookEventType(String value) {
    this.value = value;
  }

  @Override
  public String getValue() {
    return value;
  }
}
