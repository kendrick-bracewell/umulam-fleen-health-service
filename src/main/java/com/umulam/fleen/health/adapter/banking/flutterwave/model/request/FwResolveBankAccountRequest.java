package com.umulam.fleen.health.adapter.banking.flutterwave.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FwResolveBankAccountRequest {

  @JsonProperty("account_number")
  private String accountNumber;

  @JsonProperty("account_bank")
  private String bankCode;
}
