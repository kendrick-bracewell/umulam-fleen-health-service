package com.umulam.fleen.health.adapter.banking.paystack.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.umulam.fleen.health.model.event.paystack.PsChargeEvent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import static com.umulam.fleen.health.util.DateFormatUtil.DATE_TIME_WITH_TIMEZONE;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PsVerifyTransactionResponse extends PaystackResponse {

  private PsTransactionVerificationData data;

  @Getter
  @Setter
  @NoArgsConstructor
  public static class PsTransactionVerificationData {

    private Long id;
    private String status;
    private String reference;
    private Double amount;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_WITH_TIMEZONE)
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_WITH_TIMEZONE)
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    @JsonProperty("gateway_response")
    private String gatewayResponse;

    private String channel;
    private String currency;
    private Double fees;

    private PsChargeEvent.ChargeData.Authorization authorization;
    private PsChargeEvent.ChargeData.Customer customer;
  }
}
