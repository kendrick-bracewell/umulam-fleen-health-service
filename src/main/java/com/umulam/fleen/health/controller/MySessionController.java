package com.umulam.fleen.health.controller;

import com.umulam.fleen.health.model.request.search.base.SearchRequest;
import com.umulam.fleen.health.model.security.FleenUser;
import com.umulam.fleen.health.model.view.healthsession.HealthSessionView;
import com.umulam.fleen.health.model.view.search.SearchResultView;
import com.umulam.fleen.health.resolver.SearchParam;
import com.umulam.fleen.health.service.session.UserHealthSessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "sessions")
public class MySessionController {

  private final UserHealthSessionService userHealthSessionService;

  public MySessionController(UserHealthSessionService userHealthSessionService) {
    this.userHealthSessionService = userHealthSessionService;
  }

  @GetMapping(value = "/entries")
  public SearchResultView viewSessions(@AuthenticationPrincipal FleenUser user, @SearchParam SearchRequest searchRequest) {
    return userHealthSessionService.viewSessions(user, searchRequest);
  }

  @GetMapping(value = "/detail/{id}")
  public HealthSessionView healthSessionView(@AuthenticationPrincipal FleenUser user, @PathVariable(name = "id") Integer healthSessionId) {
    return userHealthSessionService.viewSessionDetail(user, healthSessionId);
  }

  public void viewTherapists() {
    
  } 
  
  public void viewTransactions() {

  }

  public void viewSessionReviews() {

  }

  public void viewPaymentDetails() {

  }
}
