package com.umulam.fleen.health.model.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.umulam.fleen.health.adapter.banking.model.PaymentRecipientType;
import com.umulam.fleen.health.model.view.base.FleenHealthView;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BankAccountView extends FleenHealthView {


  @JsonProperty("account_name")
  private String accountName;

  @JsonProperty("account_number")
  private String accountNumber;

  @JsonProperty("bank_name")
  private String bankName;

  @JsonProperty("bank_code")
  private String bankCode;

  @JsonProperty("external_system_recipient_code")
  private String externalSystemRecipientCode;

  @JsonProperty("bank_account_type")
  private String bankAccountType;

  @JsonProperty("currency")
  private String currency;

  @JsonProperty("bank_account_sub_type")
  private String bankAccountSubType;

  @JsonProperty("active")
  private boolean active;
}
