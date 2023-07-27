package com.umulam.fleen.health.service.admin;

import com.umulam.fleen.health.model.statistic.HealthSessionStatistic;
import com.umulam.fleen.health.model.statistic.SessionTransactionStatistic;

public interface AdminStatisticService {

  HealthSessionStatistic getHealthSessionStatistics();

  SessionTransactionStatistic getSessionTransactionStatistics();
}
