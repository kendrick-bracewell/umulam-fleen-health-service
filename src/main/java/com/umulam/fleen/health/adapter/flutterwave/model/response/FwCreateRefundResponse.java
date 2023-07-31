package com.umulam.fleen.health.adapter.flutterwave.model.response;

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
public class FwCreateRefundResponse {

  private String status;
  private String message;
  private CreateRefundData data;

  @Getter
  @Setter
  @NoArgsConstructor
  public static class CreateRefundData {

    private String id;

    @JsonProperty("tx_id")
    private String transactionId;

    @JsonProperty("tx_ref")
    private String transactionReference;

    @JsonProperty("flw_ref")
    private String externalSystemReference;

    private String status;
    private String destination;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_WITH_TIMEZONE)
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("account_id")
    private String accountId;

    @JsonProperty("amount_refunded")
    private String amountRefunded;

    private RefundMeta meta;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class RefundMeta {

      private String source;
    }
  }
}
