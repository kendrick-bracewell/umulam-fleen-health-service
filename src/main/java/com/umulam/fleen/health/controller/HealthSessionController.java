package com.umulam.fleen.health.controller;

import com.umulam.fleen.health.model.request.search.ProfessionalSearchRequest;
import com.umulam.fleen.health.model.view.ProfessionalViewBasic;
import com.umulam.fleen.health.model.view.SearchResultView;
import com.umulam.fleen.health.resolver.SearchParam;
import com.umulam.fleen.health.service.HealthSessionService;
import com.umulam.fleen.health.service.impl.HealthSessionServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

  public void bookSession() {

  }

  public void makePayment() {

  }

  public void confirmPayment() {

  }

  public void verifyPayment() {

  }
}
