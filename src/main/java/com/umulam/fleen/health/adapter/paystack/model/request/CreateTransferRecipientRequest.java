package com.umulam.fleen.health.adapter.paystack.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
public class CreateTransferRecipientRequest {

  private String type;
  private String name;
  private String currency;

  @JsonProperty("account_number")
  private String accountNumber;

  @JsonProperty("bank_code")
  private String bankCode;

  @Getter
  @Setter
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
