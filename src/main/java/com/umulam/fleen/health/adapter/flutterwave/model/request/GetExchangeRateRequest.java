package com.umulam.fleen.health.adapter.flutterwave.model.request;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetExchangeRateRequest {

  private String amount;
  private String destinationCurrency;
  private String sourceCurrency;
}
