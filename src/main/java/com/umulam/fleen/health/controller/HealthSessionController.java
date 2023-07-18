package com.umulam.fleen.health.controller;

import com.umulam.fleen.health.model.dto.healthsession.BookHealthSessionDto;
import com.umulam.fleen.health.model.request.search.ProfessionalSearchRequest;
import com.umulam.fleen.health.model.security.FleenUser;
import com.umulam.fleen.health.model.view.ProfessionalViewBasic;
import com.umulam.fleen.health.model.view.SearchResultView;
import com.umulam.fleen.health.resolver.SearchParam;
import com.umulam.fleen.health.service.HealthSessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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

  public void viewProfessionalAvailability() {

  }

  @PostMapping(value = "/book-session")
  public void bookSession(@Valid @RequestBody BookHealthSessionDto dto, @AuthenticationPrincipal FleenUser user) {
    healthSessionService.bookSession(dto, user);

  }

  public void makePayment() {

  }

  public void confirmPayment() {

  }

  public void verifyPayment() {

  }
}
