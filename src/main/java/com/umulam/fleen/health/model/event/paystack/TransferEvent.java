package com.umulam.fleen.health.model.event.paystack;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.umulam.fleen.health.adapter.paystack.model.enums.RecipientType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import static com.umulam.fleen.health.util.DateFormatUtil.DATE_TIME_WITH_TIMEZONE;

@Getter
@Setter
@NoArgsConstructor
public class TransferEvent {
  private String event;

  @Getter
  @Setter
  @NoArgsConstructor
  public static class TransferEventData {
    private String id;
    private String amount;
    private String currency;
    private String reason;
    private String reference;
    private String failures;
    private String source;
    private String status;
    private TransferRecipientData recipient;

    @JsonProperty("transfer_code")
    private String transferCode;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_WITH_TIMEZONE)
    @JsonProperty("transferred_at")
    private LocalDateTime transferredAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_WITH_TIMEZONE)
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_WITH_TIMEZONE)
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class TransferRecipientData {
      private boolean active;
      private String id;
      private String email;
      private String type;
      private String currency;
      private String name;

      @JsonProperty("recipient_code")
      private String recipientCode;

      @Getter
      @Setter
      @NoArgsConstructor
      public static class TransferRecipientDetails {

        @JsonProperty("account_number")
        private String accountNumber;

        @JsonProperty("account_name")
        private String accountName;

        @JsonProperty("bank_name")
        private String bankName;

        @JsonProperty("bank_code")
        private String bankCode;
      }
    }
  }
}
