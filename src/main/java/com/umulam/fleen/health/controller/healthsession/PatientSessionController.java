package com.umulam.fleen.health.controller.healthsession;

import com.umulam.fleen.health.model.request.search.base.SearchRequest;
import com.umulam.fleen.health.model.security.FleenUser;
import com.umulam.fleen.health.model.view.healthsession.HealthSessionView;
import com.umulam.fleen.health.model.view.professional.ProfessionalView;
import com.umulam.fleen.health.model.view.search.SearchResultView;
import com.umulam.fleen.health.resolver.SearchParam;
import com.umulam.fleen.health.service.session.PatientHealthSessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "user/sessions")
public class PatientSessionController {

  private final PatientHealthSessionService patientHealthSessionService;

  public PatientSessionController(PatientHealthSessionService patientHealthSessionService) {
    this.patientHealthSessionService = patientHealthSessionService;
  }

  @GetMapping(value = "/entries")
  public SearchResultView viewSessions(@AuthenticationPrincipal FleenUser user, @SearchParam SearchRequest searchRequest) {
    return patientHealthSessionService.viewSessions(user, searchRequest);
  }

  @GetMapping(value = "/detail/{id}")
  public HealthSessionView viewSessionDetail(@AuthenticationPrincipal FleenUser user, @PathVariable(name = "id") Integer healthSessionId) {
    return patientHealthSessionService.viewSessionDetail(user, healthSessionId);
  }

  @GetMapping(value = "/professionals")
  public List<ProfessionalView> viewProfessionals(@AuthenticationPrincipal FleenUser user) {
    return patientHealthSessionService.viewProfessionalsOfPatient(user);
  }

  @GetMapping(value = "/professional/detail/{id}")
  public Object viewProfessionalDetail(@PathVariable(name = "id") Integer professionalId) {
    return patientHealthSessionService.viewProfessionalDetail(professionalId);
  }
  
  public void viewTransactions() {

  }

  public void viewSessionReviews() {

  }

  public void viewPaymentDetails() {

  }
}
