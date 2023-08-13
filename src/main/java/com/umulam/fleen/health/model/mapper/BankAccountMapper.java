package com.umulam.fleen.health.model.mapper;

import com.umulam.fleen.health.model.domain.MemberBankAccount;
import com.umulam.fleen.health.model.view.BankAccountView;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

public class BankAccountMapper {

  private BankAccountMapper() { }

  public static BankAccountView toBankAccountView(MemberBankAccount entry) {
    if (nonNull(entry)) {
      return BankAccountView.builder()
        .accountNumber(entry.getAccountNumber())
        .accountName(entry.getAccountName())
        .bankName(entry.getBankName())
        .bankCode(entry.getBankCode())
        .bankAccountType(entry.getBankAccountType().name())
        .currency(entry.getCurrency())
        .bankAccountSubType(entry.getBankAccountSubType().name())
        .active(entry.isActive())
        .createdOn(entry.getCreatedOn())
        .build();
    }
    return null;
  }

  public static List<BankAccountView> toBankAccountViews(List<MemberBankAccount> entries) {
    if (entries != null && !entries.isEmpty()) {
      return entries
              .stream()
              .filter(Objects::nonNull)
              .map(BankAccountMapper::toBankAccountView)
              .collect(Collectors.toList());
    }
    return Collections.emptyList();
  }
}
