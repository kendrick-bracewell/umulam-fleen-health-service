package com.umulam.fleen.health.controller.admin;

import com.umulam.fleen.health.model.statistic.HealthSessionStatistic;
import com.umulam.fleen.health.service.admin.AdminStatisticService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "admin/statistics")
public class AdminStatisticsController {

  private final AdminStatisticService adminStatisticService;

  public AdminStatisticsController(AdminStatisticService adminStatisticService) {
    this.adminStatisticService = adminStatisticService;
  }

  @GetMapping(value = "/sessions")
  public HealthSessionStatistic viewSessions() {
    return adminStatisticService.getHealthSessionStatistics();
  }

  public void viewSessionTransactions() {

  }

  public void viewMembers() {

  }

  public void viewGeneral() {

  }
}
