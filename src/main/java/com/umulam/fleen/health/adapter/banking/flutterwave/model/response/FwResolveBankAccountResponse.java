package com.umulam.fleen.health.adapter.banking.flutterwave.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FwResolveBankAccountResponse extends FlutterwaveResponse {

  private BankAccountData data;

  @Getter
  @Setter
  @NoArgsConstructor
  public static class BankAccountData {

    @JsonProperty("account_number")
    private String accountNumber;

    @JsonProperty("account_name")
    private String accountName;
  }
}
