package com.umulam.fleen.health.service.admin.impl;

import com.umulam.fleen.health.constant.session.HealthSessionStatus;
import com.umulam.fleen.health.model.statistic.HealthSessionStatistics;
import com.umulam.fleen.health.repository.jpa.admin.statistic.AdminHealthSessionStatisticRepository;
import com.umulam.fleen.health.service.admin.AdminStatisticService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AdminStatisticServiceImpl implements AdminStatisticService {

  private final AdminHealthSessionStatisticRepository adminHealthSessionStatisticRepository;

  public AdminStatisticServiceImpl(AdminHealthSessionStatisticRepository adminHealthSessionStatisticRepository) {
    this.adminHealthSessionStatisticRepository = adminHealthSessionStatisticRepository;
  }

  @Override
  public HealthSessionStatistics getHealthSessionStatistics() {
    long totalNumberOfSessions = adminHealthSessionStatisticRepository.countTotalNumberOfSessions();
    long totalNumberOfPendingSessions = adminHealthSessionStatisticRepository.countTotalNumberOfPendingSessions(HealthSessionStatus.PENDING);
    long totalNumberOfCompletedSessions = adminHealthSessionStatisticRepository.countTotalNumberOfCompletedSessions(HealthSessionStatus.COMPLETED);
    long totalNumberOfScheduleSessions = adminHealthSessionStatisticRepository.countTotalNumberOfScheduleSessions(HealthSessionStatus.SCHEDULED);
    long totalNumberOfRescheduledSessions = adminHealthSessionStatisticRepository.countTotalNumberOfRescheduledSessions(HealthSessionStatus.RESCHEDULED);
    return HealthSessionStatistics.builder()
      .totalNumberOfSessions(totalNumberOfSessions)
      .totalNumberOfPendingSessions(totalNumberOfPendingSessions)
      .totalNumberOfCompletedSessions(totalNumberOfCompletedSessions)
      .totalNumberOfScheduleSessions(totalNumberOfScheduleSessions)
      .totalNumberOfRescheduledSessions(totalNumberOfRescheduledSessions)
      .build();
  }
}
