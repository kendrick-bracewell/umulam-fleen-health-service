package com.umulam.fleen.health.adapter.paystack.model.request;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResolveBankAccountRequest {

  private String accountNumber;
  private String bankCode;
}
