package com.umulam.fleen.health.adapter.flutterwave.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FwCreateRefundRequest {

  @JsonProperty("id")
  private String transactionId;

  @JsonProperty("amount")
  private String amountToRefund;
}
