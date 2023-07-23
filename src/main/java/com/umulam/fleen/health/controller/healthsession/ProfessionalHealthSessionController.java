package com.umulam.fleen.health.controller.healthsession;

import com.umulam.fleen.health.model.request.search.base.SearchRequest;
import com.umulam.fleen.health.model.security.FleenUser;
import com.umulam.fleen.health.model.view.healthsession.HealthSessionView;
import com.umulam.fleen.health.model.view.search.SearchResultView;
import com.umulam.fleen.health.resolver.SearchParam;
import com.umulam.fleen.health.service.session.ProfessionalHealthSessionService;
import com.umulam.fleen.health.service.transaction.SessionTransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "professional/health")
public class ProfessionalHealthSessionController {

  private final ProfessionalHealthSessionService professionalHealthSessionService;
  private final SessionTransactionService sessionTransactionService;

  public ProfessionalHealthSessionController(ProfessionalHealthSessionService professionalHealthSessionService,
                                             SessionTransactionService sessionTransactionService) {
    this.professionalHealthSessionService = professionalHealthSessionService;
    this.sessionTransactionService = sessionTransactionService;
  }

  @GetMapping(value = "/sessions/entries")
  public SearchResultView viewSessions(@AuthenticationPrincipal FleenUser user, @SearchParam SearchRequest searchRequest) {
    return professionalHealthSessionService.viewSessions(user, searchRequest);
  }

  @GetMapping(value = "/session/detail/{id}")
  public HealthSessionView viewSessionDetail(@AuthenticationPrincipal FleenUser user, @PathVariable(name = "id") Integer healthSessionId) {
    return professionalHealthSessionService.viewSessionDetail(user, healthSessionId);
  }
}
