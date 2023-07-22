package com.umulam.fleen.health.controller;

import com.umulam.fleen.health.model.dto.healthsession.BookHealthSessionDto;
import com.umulam.fleen.health.model.dto.healthsession.ReScheduleHealthSessionDto;
import com.umulam.fleen.health.model.request.search.ProfessionalSearchRequest;
import com.umulam.fleen.health.model.response.FleenHealthResponse;
import com.umulam.fleen.health.model.response.professional.GetProfessionalBookSessionResponse;
import com.umulam.fleen.health.model.response.professional.ProfessionalCheckAvailabilityResponse;
import com.umulam.fleen.health.model.security.FleenUser;
import com.umulam.fleen.health.model.view.search.ProfessionalViewBasic;
import com.umulam.fleen.health.model.view.search.SearchResultView;
import com.umulam.fleen.health.resolver.SearchParam;
import com.umulam.fleen.health.service.HealthSessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.umulam.fleen.health.constant.base.FleenHealthConstant.HEALTH_SESSION_RESCHEDULED;
import static com.umulam.fleen.health.constant.base.FleenHealthConstant.SUCCESS_MESSAGE;

@Slf4j
@RestController
@RequestMapping(value = "health")
public class HealthSessionController {

  private final HealthSessionService healthSessionService;

  public HealthSessionController(HealthSessionService healthSessionService) {
    this.healthSessionService = healthSessionService;
  }

  @GetMapping(value = "/professionals")
  public SearchResultView viewProfessionals(@SearchParam ProfessionalSearchRequest searchRequest) {
    return healthSessionService.viewProfessionals(searchRequest);
  }

  @GetMapping(value = "/professional/detail/{id}")
  public ProfessionalViewBasic viewProfessionalDetail(@PathVariable(name = "id") Integer professionalId) {
    return healthSessionService.viewProfessionalDetail(professionalId);
  }

  @GetMapping(value = "/professional/check-availability/{id}")
  public ProfessionalCheckAvailabilityResponse viewProfessionalAvailability(@AuthenticationPrincipal FleenUser user, @PathVariable(name = "id") Integer professionalId) {
    return healthSessionService.viewProfessionalAvailability(user, professionalId);
  }

  @GetMapping(value = "/book-session/{id}")
  public GetProfessionalBookSessionResponse getProfessionalBookSession(@AuthenticationPrincipal FleenUser user, @PathVariable(name = "id") Integer professionalId) {
    return healthSessionService.getProfessionalBookSession(user, professionalId);
  }

  @PostMapping(value = "/book-session")
  public FleenHealthResponse bookSession(@Valid @RequestBody BookHealthSessionDto dto, @AuthenticationPrincipal FleenUser user) {
    healthSessionService.bookSession(dto, user);
    return new FleenHealthResponse(SUCCESS_MESSAGE);
  }

  @PostMapping(value = "/reschedule-session/{id}")
  public FleenHealthResponse rescheduleSession(@Valid @RequestBody ReScheduleHealthSessionDto dto,
                                  @AuthenticationPrincipal FleenUser user,
                                  @PathVariable(name = "id") Integer healthSessionId) {
    healthSessionService.rescheduleSession(dto, user, healthSessionId);
    return new FleenHealthResponse(HEALTH_SESSION_RESCHEDULED);
  }

  public void makePayment() {

  }

  public void confirmPayment() {

  }

  public void verifyPayment() {

  }
}
