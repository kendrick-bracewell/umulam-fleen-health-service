package com.umulam.fleen.health.controller.admin;

import com.umulam.fleen.health.model.request.search.HealthSessionSearchRequest;
import com.umulam.fleen.health.model.request.search.SessionTransactionSearchRequest;
import com.umulam.fleen.health.model.request.search.base.SearchRequest;
import com.umulam.fleen.health.model.view.healthsession.HealthSessionView;
import com.umulam.fleen.health.model.view.search.SearchResultView;
import com.umulam.fleen.health.resolver.SearchParam;
import com.umulam.fleen.health.service.admin.AdminHealthSessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
  public SearchResultView viewSessions(@SearchParam HealthSessionSearchRequest searchRequest) {
    return healthSessionService.viewSessions(searchRequest);
  }

  @GetMapping(value = "/detail/{id}")
  public HealthSessionView viewSession(@PathVariable(name = "id") Integer healthSessionId) {
    return healthSessionService.viewSession(healthSessionId);
  }

  @GetMapping(value = "/reviews")
  public SearchResultView viewSessionReviews(@SearchParam SearchRequest searchRequest) {
    return healthSessionService.viewSessionReviews(searchRequest);
  }

  @GetMapping(value = "/transaction")
  public void viewTransactions(@SearchParam SessionTransactionSearchRequest searchRequest) {

  }

  @GetMapping(value = "/transaction/{id}")
  public void viewTransaction() {

  }
}
