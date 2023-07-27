package com.umulam.fleen.health.adapter.paystack.model.enums;

import com.umulam.fleen.health.adapter.EndpointBlock;

public enum PaystackEndpointBlock implements EndpointBlock {

  RESOLVE("/resolve"),
  TRANSFER("/transfer"),
  TRANSFER_RECIPIENT("/transferrecipient"),
  BANK("bank");

  private final String value;

  PaystackEndpointBlock(String value) {
    this.value = value;
  }

  @Override
  public String getValue() {
    return value;
  }
}
