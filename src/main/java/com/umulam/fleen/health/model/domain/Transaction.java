package com.umulam.fleen.health.model.domain;

import com.umulam.fleen.health.constant.session.PaymentGateway;
import com.umulam.fleen.health.constant.session.TransactionStatus;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;

public class Transaction {

  @Column
  private Integer id;

  @Column
  private String reference;

  @Column
  private String externalReference;

  @Column
  private Member payer;

  @Column
  private Member receiver;

  @CreationTimestamp
  @Column
  private LocalDateTime createdOn;

  @UpdateTimestamp
  @Column
  private LocalDateTime updatedOn;

  @Column
  @Enumerated(EnumType.STRING)
  private TransactionStatus status;

  @Column
  @Enumerated(EnumType.STRING)
  private PaymentGateway gateway;

  @Column
  private Double amount;

  @Column
  @Enumerated(EnumType.STRING)
  private String currency;
}
