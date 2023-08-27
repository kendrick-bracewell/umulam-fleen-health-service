package com.umulam.fleen.health.constant.session;

import com.umulam.fleen.health.model.domain.Member;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;

public class TransactionPaymentMethod {

  @Column
  private Long id;

  @Column
  private Member member;

  @Column
  @Enumerated(EnumType.STRING)
  private PaymentMethod method;

  @Column
  private String cardType;

  @Column
  private String cardNumber;

  @Column
  private String expiryDate;

  @Column
  private String cvv;

  @Column
  private String paymentMethodReference;

  @CreationTimestamp
  @Column
  private LocalDateTime createdOn;

  @UpdateTimestamp
  @Column
  private LocalDateTime updatedOn;

}
