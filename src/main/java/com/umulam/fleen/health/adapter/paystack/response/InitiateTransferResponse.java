package com.umulam.fleen.health.adapter.paystack.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InitiateTransferResponse {

  private boolean status;
  private String message;

  @Getter
  @Setter
  @NoArgsConstructor
  public static class InitiateTransferData {

    private String reference;
    private String currency;
    private String amount;
    private String source;
    private String recipient;
    private String status;

    @JsonProperty("transfer_code")
    private String transferCode;
  }
}
