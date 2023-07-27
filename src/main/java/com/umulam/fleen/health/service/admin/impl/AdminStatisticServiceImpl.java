package com.umulam.fleen.health.service.admin.impl;

import com.umulam.fleen.health.constant.session.HealthSessionStatus;
import com.umulam.fleen.health.constant.session.TransactionStatus;
import com.umulam.fleen.health.model.statistic.HealthSessionStatistic;
import com.umulam.fleen.health.model.statistic.SessionTransactionStatistic;
import com.umulam.fleen.health.repository.jpa.admin.statistic.AdminHealthSessionStatisticJpaRepository;
import com.umulam.fleen.health.repository.jpa.admin.statistic.AdminSessionTransactionStatisticJpaRepository;
import com.umulam.fleen.health.service.admin.AdminStatisticService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AdminStatisticServiceImpl implements AdminStatisticService {

  private final AdminHealthSessionStatisticJpaRepository adminHealthSessionStatisticJpaRepository;
  private final AdminSessionTransactionStatisticJpaRepository adminSessionTransactionStatisticJpaRepository;

  public AdminStatisticServiceImpl(AdminHealthSessionStatisticJpaRepository adminHealthSessionStatisticJpaRepository,
                                   AdminSessionTransactionStatisticJpaRepository adminSessionTransactionStatisticJpaRepository) {
    this.adminHealthSessionStatisticJpaRepository = adminHealthSessionStatisticJpaRepository;
    this.adminSessionTransactionStatisticJpaRepository = adminSessionTransactionStatisticJpaRepository;
  }

  @Override
  public HealthSessionStatistic getHealthSessionStatistics() {
    long totalNumberOfSessions = adminHealthSessionStatisticJpaRepository.countTotalNumberOfSessions();
    long totalNumberOfPendingSessions = adminHealthSessionStatisticJpaRepository.countTotalNumberOfPendingSessions(HealthSessionStatus.PENDING);
    long totalNumberOfCompletedSessions = adminHealthSessionStatisticJpaRepository.countTotalNumberOfCompletedSessions(HealthSessionStatus.COMPLETED);
    long totalNumberOfScheduleSessions = adminHealthSessionStatisticJpaRepository.countTotalNumberOfScheduleSessions(HealthSessionStatus.SCHEDULED);
    long totalNumberOfRescheduledSessions = adminHealthSessionStatisticJpaRepository.countTotalNumberOfRescheduledSessions(HealthSessionStatus.RESCHEDULED);
    return HealthSessionStatistic.builder()
      .totalNumberOfSessions(totalNumberOfSessions)
      .totalNumberOfPendingSessions(totalNumberOfPendingSessions)
      .totalNumberOfCompletedSessions(totalNumberOfCompletedSessions)
      .totalNumberOfScheduleSessions(totalNumberOfScheduleSessions)
      .totalNumberOfRescheduledSessions(totalNumberOfRescheduledSessions)
      .build();
  }

  @Override
  public SessionTransactionStatistic getSessionTransactionStatistics() {
    long totalNumberOfSessionTransactions = adminSessionTransactionStatisticJpaRepository.countTotalNumberOfSessionTransactions();
    long totalNumberOfPendingSessionTransactions = adminSessionTransactionStatisticJpaRepository.countTotalNumberOfPendingSessionTransactions(TransactionStatus.PENDING);
    long totalNumberOfSuccessfulSessionTransactions = adminSessionTransactionStatisticJpaRepository.countTotalNumberOfSuccessfulSessionTransactions(TransactionStatus.SUCCESS);
    long totalNumberOfFailedSessionTransactions = adminSessionTransactionStatisticJpaRepository.countTotalNumberOfFailedSessionTransactions(TransactionStatus.FAILED);
    long totalNumberOfCanceledSessionTransactions = adminSessionTransactionStatisticJpaRepository.countTotalNumberOfCanceledSessionTransactions(TransactionStatus.CANCELLED);
    long totalNumberOfRefundedSessionTransactions = adminSessionTransactionStatisticJpaRepository.countTotalNumberOfRefundedSessionTransactions(TransactionStatus.REFUNDED);
    return SessionTransactionStatistic.builder()
      .totalNumberOfSessionTransactions(totalNumberOfSessionTransactions)
      .totalNumberOfPendingSessionTransactions(totalNumberOfPendingSessionTransactions)
      .totalNumberOfSuccessfulSessionTransactions(totalNumberOfSuccessfulSessionTransactions)
      .totalNumberOfFailedSessionTransactions(totalNumberOfFailedSessionTransactions)
      .totalNumberOfCanceledSessionTransactions(totalNumberOfCanceledSessionTransactions)
      .totalNumberOfRefundedSessionTransactions(totalNumberOfRefundedSessionTransactions)
      .build();
  }
}
