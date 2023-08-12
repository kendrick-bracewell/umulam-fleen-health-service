package com.umulam.fleen.health.model.domain.transaction;

import com.umulam.fleen.health.model.domain.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@Entity
@PrimaryKeyJoinColumn(name = "id", referencedColumnName = "id")
public class SessionTransaction extends Transaction {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "sender")
  private Member payer;

  @Column(name = "external_system_reference")
  private String externalSystemReference;

  @Column(name = "session_reference", nullable = false)
  private String sessionReference;

  @Column(name = "total_sessions", nullable = false)
  private Integer totalSessions;

  @Column(name = "group_transaction_reference", nullable = false)
  private String groupTransactionReference;

  @Column(name = "payment_currency")
  private String paymentCurrency;

  @Column(name = "amount_in_payment_currency")
  private Double amountInPaymentCurrency;
}
