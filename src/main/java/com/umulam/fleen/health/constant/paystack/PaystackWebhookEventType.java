package com.umulam.fleen.health.constant.paystack;

import lombok.Getter;

@Getter
public enum PaystackWebhookEventType {

  CHARGE_SUCCESS("charge.success"),
  TRANSFER_FAILED("transfer.failed"),
  TRANSFER_SUCCESS("transfer.success"),
  TRANSFER_REVERSED("transfer.reversed"),
  REFUND_FAILED("refund.failed"),
  REFUND_PENDING("refund.pending"),
  REFUND_PROCESSED("refund.processed");

  private final String value;

  PaystackWebhookEventType(String value) {
    this.value = value;
  }
}
