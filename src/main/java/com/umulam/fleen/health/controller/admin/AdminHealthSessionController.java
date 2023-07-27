package com.umulam.fleen.health.controller.admin;

import com.umulam.fleen.health.model.request.search.HealthSessionSearchRequest;
import com.umulam.fleen.health.model.request.search.SessionTransactionSearchRequest;
import com.umulam.fleen.health.resolver.SearchParam;
import com.umulam.fleen.health.service.admin.AdminHealthSessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "admin/health/session")
public class AdminHealthSessionController {

  private final AdminHealthSessionService healthSessionService;

  public AdminHealthSessionController(AdminHealthSessionService healthSessionService) {
    this.healthSessionService = healthSessionService;
  }

  @GetMapping(value = "/entries")
  public void viewSessions(@SearchParam HealthSessionSearchRequest searchRequest) {

  }

  @GetMapping(value = "/detail/{id}")
  public void viewSession() {

  }

  @GetMapping(value = "/reviews")
  public void viewSessionReviews() {

  }

  @GetMapping(value = "/transaction")
  public void viewTransactions(@SearchParam SessionTransactionSearchRequest searchRequest) {

  }

  @GetMapping(value = "/transaction/{id}")
  public void viewTransaction() {

  }
}
