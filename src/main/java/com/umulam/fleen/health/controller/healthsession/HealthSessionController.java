package com.umulam.fleen.health.controller.healthsession;

import com.umulam.fleen.health.model.dto.healthsession.AddHealthSessionReviewDto;
import com.umulam.fleen.health.model.dto.healthsession.BookHealthSessionDto;
import com.umulam.fleen.health.model.dto.healthsession.ReScheduleHealthSessionDto;
import com.umulam.fleen.health.model.request.search.ProfessionalSearchRequest;
import com.umulam.fleen.health.model.request.search.base.SearchRequest;
import com.umulam.fleen.health.model.response.FleenHealthResponse;
import com.umulam.fleen.health.model.response.healthsession.GetProfessionalBookSessionResponse;
import com.umulam.fleen.health.model.response.healthsession.PendingHealthSessionBookingResponse;
import com.umulam.fleen.health.model.response.healthsession.ProfessionalCheckAvailabilityResponse;
import com.umulam.fleen.health.model.security.FleenUser;
import com.umulam.fleen.health.model.view.healthsession.HealthSessionView;
import com.umulam.fleen.health.model.view.professional.ProfessionalViewBasic;
import com.umulam.fleen.health.model.view.search.SearchResultView;
import com.umulam.fleen.health.resolver.SearchParam;
import com.umulam.fleen.health.service.session.HealthSessionService;
import com.umulam.fleen.health.service.session.PatientHealthSessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.umulam.fleen.health.constant.base.FleenHealthConstant.*;

@Slf4j
@RestController
@RequestMapping(value = "health")
public class HealthSessionController {

  private final HealthSessionService healthSessionService;
  private final PatientHealthSessionService patientHealthSessionService;

  public HealthSessionController(HealthSessionService healthSessionService,
                                 PatientHealthSessionService patientHealthSessionService) {
    this.healthSessionService = healthSessionService;
    this.patientHealthSessionService = patientHealthSessionService;
  }

  @GetMapping(value = "/professionals")
  public SearchResultView viewProfessionals(@SearchParam ProfessionalSearchRequest searchRequest) {
    return healthSessionService.viewProfessionals(searchRequest);
  }

  @GetMapping(value = "/professional/detail/{id}")
  public ProfessionalViewBasic viewProfessionalDetail(@PathVariable(name = "id") Long professionalId) {
    return healthSessionService.viewProfessionalDetail(professionalId);
  }

  @GetMapping(value = "/professional/check-availability/{id}")
  public ProfessionalCheckAvailabilityResponse viewProfessionalAvailability(@AuthenticationPrincipal FleenUser user, @PathVariable(name = "id") Long professionalId) {
    return healthSessionService.viewProfessionalAvailability(user, professionalId);
  }

  @GetMapping(value = "/professional/book-session/{id}")
  public GetProfessionalBookSessionResponse getProfessionalBookSession(@PathVariable(name = "id") Long professionalId) {
    return healthSessionService.getProfessionalBookSession(professionalId);
  }

  @PostMapping(value = "professional/book-session")
  public PendingHealthSessionBookingResponse bookSession(@Valid @RequestBody BookHealthSessionDto dto, @AuthenticationPrincipal FleenUser user) {
    return healthSessionService.bookSession(dto, user);
  }

  @GetMapping(value = "/session/entries")
  public SearchResultView viewSessions(@AuthenticationPrincipal FleenUser user, @SearchParam SearchRequest searchRequest) {
    return patientHealthSessionService.viewSessions(user, searchRequest);
  }

  @GetMapping(value = "/session/detail/{id}")
  public HealthSessionView viewSessionDetail(@AuthenticationPrincipal FleenUser user, @PathVariable(name = "id") Long healthSessionId) {
    return patientHealthSessionService.viewSessionDetail(user, healthSessionId);
  }

  @PutMapping(value = "session/reschedule-session/{id}")
  public FleenHealthResponse rescheduleSession(@Valid @RequestBody ReScheduleHealthSessionDto dto,
                                  @AuthenticationPrincipal FleenUser user,
                                  @PathVariable(name = "id") Long healthSessionId) {
    healthSessionService.rescheduleSession(dto, user, healthSessionId);
    return new FleenHealthResponse(HEALTH_SESSION_RESCHEDULED);
  }

  @PutMapping(value = "session/cancel-session/{id}")
  public FleenHealthResponse cancelSession(@AuthenticationPrincipal FleenUser user, @PathVariable(name = "id") Long healthSessionId) {
    healthSessionService.cancelSession(user, healthSessionId);
    return new FleenHealthResponse(HEALTH_SESSION_CANCELED);
  }

  @PutMapping(value = "/session/add-review/{id}")
  public FleenHealthResponse addSessionReview(@Valid @RequestBody AddHealthSessionReviewDto dto,
                               @AuthenticationPrincipal FleenUser user,
                               @PathVariable(name = "id") Long sessionId) {
    healthSessionService.addSessionReview(dto, user, sessionId);
    return new FleenHealthResponse(HEALTH_SESSION_REVIEW_ADDED);
  }

}
 
