package com.umulam.fleen.health.adapter.flutterwave.model.request;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FwGetExchangeRateRequest {

  private String amount;
  private String destinationCurrency;
  private String sourceCurrency;
}
