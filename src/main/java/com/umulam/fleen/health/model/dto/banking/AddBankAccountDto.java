package com.umulam.fleen.health.model.dto.banking;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.umulam.fleen.health.adapter.banking.model.PaymentRecipientType;
import com.umulam.fleen.health.constant.session.BankAccountType;
import com.umulam.fleen.health.constant.session.CurrencyType;
import com.umulam.fleen.health.model.domain.MemberBankAccount;
import com.umulam.fleen.health.validator.EnumValid;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddBankAccountDto {

  @NotBlank(message = "{banking.accountNumber.notNull}")
  @Size(min = 10, max = 10, message = "{banking.accountNumber.size}")
  @JsonProperty("account_number")
  private String accountNumber;

  @NotBlank(message = "{banking.bankCode.notNull}")
  @Size(min = 2, max = 15, message = "{banking.bankCode.size}")
  @JsonProperty("bank_code")
  private String bankCode;

  @NotNull(message = "{banking.accountType.notNull}")
  @EnumValid(enumClass = BankAccountType.class, message = "{banking.accountType.type}")
  @JsonProperty("account_type")
  private String accountType;

  @NotNull(message = "{banking.recipientType.notNull}")
  @EnumValid(enumClass = PaymentRecipientType.class, message = "{banking.recipientType.type}")
  @JsonProperty("recipient_type")
  private String recipientType;

  @NotBlank(message = "{banking.currency.notNull}")
  @EnumValid(enumClass = CurrencyType.class, message = "{banking.currency.type}")
  @JsonProperty("currency")
  private String currency;

  public MemberBankAccount toBankAccount() {
    return MemberBankAccount.builder()
      .accountNumber(accountNumber)
      .bankAccountType(BankAccountType.valueOf(accountType))
      .currency(currency)
      .build();
  }
}
