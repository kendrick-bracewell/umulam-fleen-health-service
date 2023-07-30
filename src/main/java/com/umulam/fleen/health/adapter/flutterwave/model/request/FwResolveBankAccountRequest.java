package com.umulam.fleen.health.adapter.flutterwave.model.request;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FwResolveBankAccountRequest {

  private String accountNumber;
  private String bankCode;
}
