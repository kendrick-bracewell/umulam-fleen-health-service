package com.umulam.fleen.health.adapter.flutterwave.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FwCreateTransferRequest {

  @JsonProperty("account_bank")
  private String bankCode;

  @JsonProperty("account_number")
  private String accountNumber;

  @JsonProperty("debit_currency")
  private String sourceCurrency;

  @JsonProperty("currency")
  private String destinationCurrency;

  @JsonProperty("amount")
  private String destinationAmount;

  @JsonProperty("reference")
  private String transactionReference;

  @JsonProperty("narration")
  private String description;

  @JsonProperty("beneficiary_name")
  private String beneficiaryName;

  @Getter
  @Setter
  @NoArgsConstructor
  public static class CreateTransferMetadata {

    @JsonProperty("mobile_number")
    private String mobileNumber;

    @JsonProperty("email")
    private String emailAddress;


  }
}
