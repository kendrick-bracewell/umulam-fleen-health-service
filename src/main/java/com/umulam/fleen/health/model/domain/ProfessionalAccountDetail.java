package com.umulam.fleen.health.model.domain;


import com.umulam.fleen.health.constant.session.BankAccontType;

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
  private BankAccontType bankAccontType;

  @Column
  private String accountName;
}
