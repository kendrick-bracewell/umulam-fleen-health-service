package com.umulam.fleen.health.adapter.paystack.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import static com.umulam.fleen.health.util.DateFormatUtil.DATE_TIME_WITH_TIMEZONE;

@Getter
@Setter
@NoArgsConstructor
public class CreateTransferRecipientResponse extends PaystackResponse {

  private CreateTransferRecipientData data;

  @Getter
  @Setter
  @NoArgsConstructor
  public static class CreateTransferRecipientData {
    private boolean active;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_WITH_TIMEZONE)
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_WITH_TIMEZONE)
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    @JsonProperty("recipient_code")
    private String recipientCode;

    private String id;
    private String currency;
    private String name;
    private String type;
    private CreateTransferRecipientDetails details;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class CreateTransferRecipientDetails {

      @JsonProperty("account_number")
      private String accountNumber;

      @JsonProperty("account_name")
      private String accountName;

      @JsonProperty("bank_code")
      private String bankCode;

      @JsonProperty("bank_name")
      private String bankName;
    }

  }

}
