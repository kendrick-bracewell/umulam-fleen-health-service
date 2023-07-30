package com.umulam.fleen.health.model.event.flutterwave;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import static com.umulam.fleen.health.util.DateFormatUtil.DATE_TIME_WITH_TIMEZONE;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FwTransferEvent {

  private String event;

  @JsonProperty("event.type")
  private String eventType;

  private TransferData data;

  @Getter
  @Setter
  @NoArgsConstructor
  public static class TransferData {

    @JsonProperty("id")
    private String transferId;

    @JsonProperty("account_number")
    private String accountNumber;

    @JsonProperty("bank_name")
    private String bankName;

    @JsonProperty("bank_code")
    private String bankCode;

    @JsonProperty("full_name")
    private String fullName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_WITH_TIMEZONE)
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("debit_currency")
    private String debitCurrency;

    private String amount;
    private String fee;
    private String status;

    @JsonProperty("reference")
    private String transferReference;

    @JsonProperty("narration")
    private String transferNarration;

    @JsonProperty("complete_message")
    private String transferMessage;

    @JsonProperty("requires_approval")
    private String requiresApproval;

    @JsonProperty("is_approved")
    private String isApproved;
  }
}
