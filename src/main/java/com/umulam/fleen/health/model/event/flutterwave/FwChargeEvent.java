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
public class FwChargeEvent {

  private String event;
  private ChargeData data;

  @JsonProperty("event.type")
  private String eventType;

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ChargeData {

    private String id;

    @JsonProperty("tx_ref")
    private String transactionReference;

    @JsonProperty("flw_ref")
    private String externalSystemReference;

    private String amount;
    private String currency;

    @JsonProperty("charged_amount")
    private String chargedAmount;

    @JsonProperty("app_fee")
    private String fee;

    private String status;

    @JsonProperty("payment_type")
    private String paymentType;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_WITH_TIMEZONE)
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("account_id")
    private String accountId;

    private Customer customer;
    private Card card;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Customer {

      private String name;

      @JsonProperty("phone_number")
      private String phoneNumber;

      @JsonProperty("email")
      private String emailAddress;

      @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_WITH_TIMEZONE)
      @JsonProperty("created_at")
      private LocalDateTime createdAt;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Card {

      @JsonProperty("first_6digits")
      private String firstSixDigits;

      @JsonProperty("last_4digits")
      private String lastFourDigits;

      @JsonProperty("type")
      private String cardType;

      @JsonProperty("expiry")
      private String expiryDate;

      @JsonProperty("country")
      private String country;

      private String issuer;
    }
  }
}
