package com.umulam.fleen.health.constant.session;

import lombok.Getter;

@Getter
public enum TransactionStatus {

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
}
