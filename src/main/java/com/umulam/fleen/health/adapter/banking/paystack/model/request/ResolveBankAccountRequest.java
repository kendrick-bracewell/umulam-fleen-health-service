package com.umulam.fleen.health.adapter.banking.paystack.model.request;

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
