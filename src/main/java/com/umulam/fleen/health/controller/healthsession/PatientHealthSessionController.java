package com.umulam.fleen.health.controller.healthsession;

import com.umulam.fleen.health.model.request.search.base.SearchRequest;
import com.umulam.fleen.health.model.security.FleenUser;
import com.umulam.fleen.health.model.view.healthsession.HealthSessionReviewView;
import com.umulam.fleen.health.model.view.healthsession.HealthSessionView;
import com.umulam.fleen.health.model.view.search.SearchResultView;
import com.umulam.fleen.health.model.view.transaction.SessionTransactionView;
import com.umulam.fleen.health.resolver.SearchParam;
import com.umulam.fleen.health.service.session.PatientHealthSessionService;
import com.umulam.fleen.health.service.transaction.SessionTransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "health/patient")
public class PatientHealthSessionController {

  private final PatientHealthSessionService patientHealthSessionService;
  private final SessionTransactionService sessionTransactionService;

  public PatientHealthSessionController(PatientHealthSessionService patientHealthSessionService,
                                        SessionTransactionService sessionTransactionService) {
    this.patientHealthSessionService = patientHealthSessionService;
    this.sessionTransactionService = sessionTransactionService;
  }

  @GetMapping(value = "/session/entries")
  public SearchResultView viewSessions(@AuthenticationPrincipal FleenUser user, @SearchParam SearchRequest searchRequest) {
    return patientHealthSessionService.viewSessions(user, searchRequest);
  }

  @GetMapping(value = "/session/detail/{id}")
  public HealthSessionView viewSessionDetail(@AuthenticationPrincipal FleenUser user, @PathVariable(name = "id") Integer healthSessionId) {
    return patientHealthSessionService.viewSessionDetail(user, healthSessionId);
  }

  @GetMapping(value = "/professionals")
  public SearchResultView viewProfessionals(@AuthenticationPrincipal FleenUser user) {
    return patientHealthSessionService.viewProfessionalsOfPatient(user);
  }

  @GetMapping(value = "/professional/detail/{id}")
  public Object viewProfessionalDetail(@PathVariable(name = "id") Integer professionalId) {
    return patientHealthSessionService.viewProfessionalDetail(professionalId);
  }

  @GetMapping(value = "/transaction/entries")
  public SearchResultView viewTransactions(@AuthenticationPrincipal FleenUser user, @SearchParam SearchRequest searchRequest) {
    return sessionTransactionService.viewUserTransactions(user, searchRequest);
  }

  @GetMapping(value = "/transaction/detail/{id}")
  public SessionTransactionView viewTransactionDetail(@AuthenticationPrincipal FleenUser user, @PathVariable(name = "id") Integer transactionId) {
    return sessionTransactionService.viewUserTransactionDetail(user, transactionId);
  }

  @GetMapping(value = "/sessions/reviews")
  public List<HealthSessionReviewView> viewSessionReviews(@AuthenticationPrincipal FleenUser user) {
    return patientHealthSessionService.viewReviews(user);
  }

  public void viewPaymentDetails() {

  }
}
