package com.umulam.fleen.health.model.event.base;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WithdrawalTransferValidation {

  private String reference;
  private String status;
  private String accountNumber;
  private String bankName;
  private String bankCode;
  private String currency;
  private Double amount;
  private String fullName;
  private Double fee;
  private String externalTransferReferenceOrCode;
}
