package com.umulam.fleen.health.adapter.banking.paystack.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateTransferRecipientRequest {

  private String type;

  @JsonProperty("name")
  private String accountName;
  private String currency;

  @JsonProperty("account_number")
  private String accountNumber;

  @JsonProperty("bank_code")
  private String bankCode;

  private CreateTransferRecipientMetadata metadata;

  @Builder
  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class CreateTransferRecipientMetadata {

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("email_address")
    private String emailAddress;

    @JsonProperty("phone_number")
    private String phoneNumber;
  }
}
 
 
