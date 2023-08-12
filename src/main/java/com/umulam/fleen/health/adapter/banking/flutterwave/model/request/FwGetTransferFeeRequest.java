package com.umulam.fleen.health.adapter.banking.flutterwave.model.request;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FwGetTransferFeeRequest {

  private String amount;
  private String currency;
  private String type;
}
