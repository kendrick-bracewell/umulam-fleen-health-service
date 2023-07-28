package com.umulam.fleen.health.adapter.paystack.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InitiateTransferRequest {

  private String source;
  private String amount;
  private String reference;
  private String recipient;
  private String reason;
}
