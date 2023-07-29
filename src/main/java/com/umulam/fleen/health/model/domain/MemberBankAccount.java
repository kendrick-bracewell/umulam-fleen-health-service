package com.umulam.fleen.health.model.domain;


import com.umulam.fleen.health.constant.session.BankAccountType;
import lombok.*;

import javax.persistence.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "bank_account", uniqueConstraints = {
  @UniqueConstraint(columnNames = {"account_number"}),
  @UniqueConstraint(columnNames = {"external_system_recipient_code"})
})
public class MemberBankAccount {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;

  @Column(name = "account_name", nullable = false, length = 200)
  private String accountName;

  @Column(name = "account_number", nullable = false, length = 20)
  private String accountNumber;

  @Column(name = "bank_name", nullable = false)
  private String bankName;

  @Column(name = "external_system_recipient_code", nullable = false)
  private String externalSystemRecipientCode;

  @Column(name = "banking_detail_type", nullable = false)
  @Enumerated(EnumType.STRING)
  private BankAccountType bankAccountType;

  @Column(name = "currency", nullable = false, length = 5)
  private String currency;
}
