package com.umulam.fleen.health.adapter.flutterwave.model.request;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FxGetTransferFeeRequest {

  private String amount;
  private String currency;
  private String type;
}
