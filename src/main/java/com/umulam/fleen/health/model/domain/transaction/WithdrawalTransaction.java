package com.umulam.fleen.health.model.domain.transaction;

import com.umulam.fleen.health.constant.session.WithdrawalStatus;
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
public class WithdrawalTransaction extends Transaction {

  @Column(name = "withdrawal_status", nullable = false)
  @Enumerated(EnumType.STRING)
  private WithdrawalStatus withdrawalStatus;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "recipient_id", nullable = false)
  private Member recipient;

  @Column(name = "account_name", nullable = false)
  private String accountName;

  @Column(name = "account_number", nullable = false)
  private String accountNumber;

  @Column(name = "bank_name", nullable = false)
  private String bankName;

  @Column(name = "bank_code", nullable = false)
  private String bankCode;

  @Column(name = "external_system_reference")
  private String externalSystemReference;

  @Column(name = "currency", nullable = false)
  private String currency;
}
