package com.umulam.fleen.health.model.domain.transaction;

import com.umulam.fleen.health.constant.session.PaymentGateway;
import com.umulam.fleen.health.constant.session.TransactionStatus;
import com.umulam.fleen.health.constant.session.TransactionSubType;
import com.umulam.fleen.health.constant.session.TransactionType;
import com.umulam.fleen.health.model.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "transaction", indexes = {
  @Index(columnList = "reference", name = "tx_ref_index", unique = true)
})
public class Transaction {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;

  @Column(name = "reference", nullable = false)
  private String reference;

  @CreationTimestamp
  @Column(name = "created_on", nullable = false, updatable = false)
  private LocalDateTime createdOn;

  @UpdateTimestamp
  @Column(name = "updated_on", nullable = false)
  private LocalDateTime updatedOn;

  @Column(name = "status", nullable = false)
  @Enumerated(EnumType.STRING)
  private TransactionStatus status;

  @Column(name = "gateway", nullable = false)
  @Enumerated(EnumType.STRING)
  private PaymentGateway gateway;

  @Column(name = "type", nullable = false)
  @Enumerated(EnumType.STRING)
  private TransactionType type;

  @Column(name = "sub_type", nullable = false)
  @Enumerated(EnumType.STRING)
  private TransactionSubType subType;

  @Column(name = "amount", nullable = false)
  private Double amount;

  @Column(name = "currency")
  private String currency;
}
