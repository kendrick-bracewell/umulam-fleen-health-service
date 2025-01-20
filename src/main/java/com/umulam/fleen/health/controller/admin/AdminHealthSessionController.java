package com.umulam.fleen.health.controller.admin;

import com.umulam.fleen.health.model.request.search.HealthSessionSearchRequest;
import com.umulam.fleen.health.model.request.search.SessionTransactionSearchRequest;
import com.umulam.fleen.health.model.request.search.base.SearchRequest;
import com.umulam.fleen.health.model.response.FleenHealthResponse;
import com.umulam.fleen.health.model.security.FleenUser;
import com.umulam.fleen.health.model.view.healthsession.HealthSessionView;
import com.umulam.fleen.health.model.view.search.SearchResultView;
import com.umulam.fleen.health.model.view.transaction.SessionTransactionView;
import com.umulam.fleen.health.resolver.SearchParam;
import com.umulam.fleen.health.service.admin.AdminHealthSessionService;
import com.umulam.fleen.health.service.admin.AdminTransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.umulam.fleen.health.constant.base.FleenHealthConstant.HEALTH_SESSION_CANCELED;

@Slf4j
@RestController
@RequestMapping(value = "admin/health/session")
public class AdminHealthSessionController {

  private final AdminHealthSessionService healthSessionService;
  private final AdminTransactionService transactionService;

  public AdminHealthSessionController(@Qualifier("adminHealthSessionService") AdminHealthSessionService healthSessionService,
                                      AdminTransactionService transactionService) {
    this.healthSessionService = healthSessionService;
    this.transactionService = transactionService;
  }

  @GetMapping(value = "/entries")
  public SearchResultView viewSessions(@SearchParam HealthSessionSearchRequest searchRequest) {
    return healthSessionService.viewSessions(searchRequest);
  }

  @GetMapping(value = "/detail/{id}")
  public HealthSessionView viewSession(@PathVariable(name = "id") Long healthSessionId) {
    return healthSessionService.viewSession(healthSessionId);
  }

  @GetMapping(value = "/reviews")
  public SearchResultView viewSessionReviews(@SearchParam SearchRequest searchRequest) {
    return healthSessionService.viewSessionReviews(searchRequest);
  }

  @GetMapping(value = "/transaction")
  public SearchResultView viewSessionTransactions(@SearchParam SessionTransactionSearchRequest searchRequest) {
    return transactionService.viewSessionTransactions(searchRequest);
  }

  @GetMapping(value = "/transaction/{id}")
  public SessionTransactionView viewSessionTransaction(@PathVariable(name = "id") Long sessionTransactionId) {
    return transactionService.viewSessionTransaction(sessionTransactionId);
  }

  @PutMapping(value = "/cancel-session/{id}")
  public FleenHealthResponse cancelSession(@PathVariable(name = "id") Long healthSessionId) {
    healthSessionService.cancelSession(healthSessionId);
    return new FleenHealthResponse(HEALTH_SESSION_CANCELED);
  }
}
 
 
