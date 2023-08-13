package com.umulam.fleen.health.model.event.paystack;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.umulam.fleen.health.constant.session.TransactionServiceType;
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
public class PsChargeEvent {

  private String event;
  private ChargeData data;

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ChargeData {

    private String status;
    private String reference;
    private Double amount;

    @JsonProperty("gateway_response")
    private String gatewayResponse;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_WITH_TIMEZONE)
    @JsonProperty("paid_at")
    private LocalDateTime paidAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_WITH_TIMEZONE)
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    private String channel;

    @JsonProperty("ip_address")
    private String ipAddress;

    private String currency;

    private Double fees;

    @JsonProperty("order_id")
    private String orderId;

    private ChargeMetaData metadata;
    private Authorization authorization;
    private Customer customer;
    private PaymentSource source;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChargeMetaData {

      @JsonProperty("transaction_reference")
      private String transactionReference;

      @JsonProperty("health_session_id")
      private String healthSessionId;

      @JsonProperty("transaction_type")
      private TransactionServiceType transactionType;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Authorization {

      private String bin;
      private String last4;

      @JsonProperty("exp_month")
      private String expMonth;

      @JsonProperty("exp_year")
      private String expYear;
      private String channel;

      @JsonProperty("card_type")
      private String cardType;
      private String brand;
      private String bank;

      @JsonProperty("country_code")
      private String countryCode;

      @JsonProperty("account_name")
      private String accountName;

      @JsonProperty("receiver_bank_account_number")
      private String receiverBankAccountNumber;

      @JsonProperty("receiver_bank")
      private String receiverBank;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Customer {

      @JsonProperty("first_name")
      private String firstName;

      @JsonProperty("last_name")
      private String lastName;

      private String email;
      private String phone;

      @JsonProperty("international_format_phone")
      private String internationalFormatPhone;

      @JsonProperty("customer_code")
      private String customerCode;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaymentSource {

      private String type;
      private String source;

      @JsonProperty("entry_point")
      private String entryPoint;
    }
  }
}
