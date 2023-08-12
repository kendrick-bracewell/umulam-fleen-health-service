package com.umulam.fleen.health.adapter.banking.flutterwave.model.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
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
  private Double amount;

  @JsonProperty("reference")
  private String transactionReference;

  @JsonProperty("narration")
  private String description;

  @JsonProperty("beneficiary_name")
  private String beneficiaryName;

  @JsonProperty("destination_branch_code")
  private String destinationBranchCode;

  private CreateTransferMetadata meta;

  @Builder
  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  @JsonInclude(JsonInclude.Include.NON_NULL)
  public static class CreateTransferMetadata {

    @JsonProperty("mobile_number")
    private String mobileNumber;

    @JsonProperty("email")
    private String emailAddress;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("beneficiary_country")
    private String countryCode;

    @JsonProperty("sender_country")
    private String senderCountryCode;

    @JsonProperty("sender")
    private String issuer;

    @JsonProperty("merchant_name")
    private String merchantName;

    @JsonProperty("sender_city")
    private String city;

    @JsonProperty("recipient_address")
    private String recipientAddress;
  }
}
