package com.umulam.fleen.health.model.domain;


import com.umulam.fleen.health.constant.session.BankAccountType;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

public class ProfessionalAccountDetail {

  @Column
  private Integer id;

  @Column
  private String accountNumber;

  @Column
  private String bankName;

  @Column
  @Enumerated(EnumType.STRING)
  private BankAccountType bankAccountType;

  @Column
  private String accountName;
}
