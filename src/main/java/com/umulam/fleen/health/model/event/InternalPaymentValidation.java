package com.umulam.fleen.health.model.event;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InternalPaymentValidation {

  private String status;
  private String transactionReference;
  private String externalSystemTransactionReference;
  private String currency;
}
