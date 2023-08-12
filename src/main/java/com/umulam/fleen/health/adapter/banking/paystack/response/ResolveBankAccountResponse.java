package com.umulam.fleen.health.adapter.banking.paystack.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResolveBankAccountResponse extends PaystackResponse {

  private BankAccountData data;

  @Getter
  @Setter
  @NoArgsConstructor
  public static class BankAccountData {

    @JsonProperty("account_number")
    private String accountNumber;

    @JsonProperty("account_name")
    private String accountName;

    @JsonProperty("account_id")
    private String accountId;
  }
}
