package com.umulam.fleen.health.model.domain;


import com.umulam.fleen.health.constant.session.BankAccountType;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "bank_account", uniqueConstraints = {
  @UniqueConstraint(columnNames = {"account_number"})
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

  @Column(name = "bank_code", nullable = false)
  private String bankCode;

  @Column(name = "external_system_recipient_code")
  private String externalSystemRecipientCode;

  @Column(name = "banking_detail_type", nullable = false)
  @Enumerated(EnumType.STRING)
  private BankAccountType bankAccountType;

  @Column(name = "currency", nullable = false, length = 5)
  private String currency;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;

  @CreationTimestamp
  @Column(name = "created_on", updatable = false)
  private LocalDateTime createdOn;

  @Column(name = "active")
  private boolean active;
}
