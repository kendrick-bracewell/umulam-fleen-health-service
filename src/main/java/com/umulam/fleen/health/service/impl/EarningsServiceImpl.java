package com.umulam.fleen.health.service.impl;

import com.umulam.fleen.health.model.domain.Earnings;
import com.umulam.fleen.health.model.domain.Member;
import com.umulam.fleen.health.model.domain.transaction.WithdrawalTransaction;
import com.umulam.fleen.health.repository.jpa.EarningsJpaRepository;
import com.umulam.fleen.health.service.EarningsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Slf4j
@Service
public class EarningsServiceImpl implements EarningsService {

  private final EarningsJpaRepository earningsJpaRepository;

  public EarningsServiceImpl(EarningsJpaRepository earningsJpaRepository) {
    this.earningsJpaRepository = earningsJpaRepository;
  }

  @Override
  @Transactional
  public void reverseTransactionAndUpdateEarnings(WithdrawalTransaction transaction) {
    if (transaction == null) {
      return;
    }

    Member member = transaction.getRecipient();
    Optional<Earnings> earningsExists = earningsJpaRepository.findByMember(member);
    if (earningsExists.isEmpty()) {
      return;
    }

    Earnings earnings = earningsExists.get();
    BigDecimal newEarningsBalance = earnings.getTotalEarnings().add(BigDecimal.valueOf(transaction.getAmount()));
    earnings.setTotalEarnings(newEarningsBalance);

    earningsJpaRepository.save(earnings);
  }
}
