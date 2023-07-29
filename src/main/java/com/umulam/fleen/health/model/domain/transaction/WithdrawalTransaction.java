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

  @Column(name = "recipient_name")
  private String recipientName;

  @Column(name = "account_number")
  private String accountNumber;

  @Column(name = "bank_name")
  private String bankName;

  @Column(name = "bank_code")
  private String bankCode;
}
