package com.umulam.fleen.health.service.impl;

import com.umulam.fleen.health.constant.session.HealthSessionStatus;
import com.umulam.fleen.health.model.domain.Earnings;
import com.umulam.fleen.health.model.domain.HealthSession;
import com.umulam.fleen.health.model.domain.Member;
import com.umulam.fleen.health.model.domain.transaction.SessionTransaction;
import com.umulam.fleen.health.repository.jpa.EarningsJpaRepository;
import com.umulam.fleen.health.repository.jpa.HealthSessionJpaRepository;
import com.umulam.fleen.health.repository.jpa.transaction.SessionTransactionJpaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class FleenHealthSchedulerService {

  private final EarningsJpaRepository earningsJpaRepository;
  private final HealthSessionJpaRepository healthSessionJpaRepository;
  private final SessionTransactionJpaRepository sessionTransactionJpaRepository;
  public FleenHealthSchedulerService(EarningsJpaRepository earningsJpaRepository,
                                     HealthSessionJpaRepository healthSessionJpaRepository,
                                     SessionTransactionJpaRepository sessionTransactionJpaRepository) {
    this.earningsJpaRepository = earningsJpaRepository;
    this.healthSessionJpaRepository = healthSessionJpaRepository;
    this.sessionTransactionJpaRepository = sessionTransactionJpaRepository;
  }

  @Scheduled(cron = "0 0 0,2,4,6,8,10,12,14,16,18,20,22,23 * * *")
  public void updateSessionsAndEarnings() {
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime yesterday = now.minusDays(1);

    List<HealthSession> healthSessions = healthSessionJpaRepository.findByStatusOrStatusAndDate(HealthSessionStatus.SCHEDULED, HealthSessionStatus.RESCHEDULED, yesterday.toLocalDate());
    if (healthSessions.isEmpty()) {
      return;
    }

    Earnings earnings = Earnings.builder().build();
    HashMap<Long, Earnings> memberEarningsAccount = new HashMap<>();
    List<HealthSession> updatedHealthSessions = new ArrayList<>();
    for (HealthSession healthSession : healthSessions) {
      Member professional = healthSession.getProfessional();

      if (memberEarningsAccount.containsKey(professional.getId())) {
        earnings = memberEarningsAccount.get(professional.getId());
      }

      if (!memberEarningsAccount.containsKey(professional.getId())) {
        Optional<Earnings> earningsExists = earningsJpaRepository.findByMember(professional);
        if (earningsExists.isEmpty()) {
          earnings.setMember(professional);
          earningsJpaRepository.save(earnings);
        }
        memberEarningsAccount.put(professional.getId(), earnings);
      }

      Optional<SessionTransaction> sessionTransactionExists = sessionTransactionJpaRepository.findBySessionReference(healthSession.getReference());
      if (sessionTransactionExists.isEmpty()) {
        continue;
      }

      SessionTransaction sessionTransaction = sessionTransactionExists.get();
      BigDecimal updatedEarnings = earnings.getTotalEarnings().add(BigDecimal.valueOf(sessionTransaction.getAmountInPaymentCurrency() * getEarningsPercentage()));
      earnings.setTotalEarnings(updatedEarnings);
      healthSession.setStatus(HealthSessionStatus.COMPLETED);
      updatedHealthSessions.add(healthSession);
    }

    healthSessionJpaRepository.saveAll(updatedHealthSessions);
    earningsJpaRepository.saveAll(new ArrayList<>(memberEarningsAccount.values()));
  }

  private double getEarningsPercentage() {
    return 0.68;
  }

}
