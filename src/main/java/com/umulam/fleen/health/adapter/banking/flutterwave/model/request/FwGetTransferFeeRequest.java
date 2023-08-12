package com.umulam.fleen.health.adapter.banking.flutterwave.model.request;

import com.umulam.fleen.health.model.domain.MemberBankAccount;
import lombok.*;

import static com.umulam.fleen.health.adapter.banking.model.PaymentRecipientType.MOBILE_MONEY;
import static com.umulam.fleen.health.adapter.banking.model.PaymentRecipientType.NUBAN;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FwGetTransferFeeRequest {

  private String amount;
  private String currency;
  private String type;

  public void setTransferType(MemberBankAccount bankAccount) {
    if (bankAccount.getBankAccountSubType() == MOBILE_MONEY) {
      type = bankAccount.getBankAccountSubType().getOtherName();
    } else if (bankAccount.getBankAccountSubType() == NUBAN) {
      type = bankAccount.getBankAccountSubType().getOtherName();
    }
  }
}
