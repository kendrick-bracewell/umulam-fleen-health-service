package com.umulam.fleen.health.controller;

import com.umulam.fleen.health.model.dto.professional.UpdateProfessionalDetailsDto;
import com.umulam.fleen.health.model.security.FleenUser;
import com.umulam.fleen.health.service.ProfessionalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(value = "professional")
public class ProfessionalController {

  private final ProfessionalService professionalService;

  public ProfessionalController(ProfessionalService professionalService) {
    this.professionalService = professionalService;
  }

  @GetMapping(value = "/verification/update-details")
  public Object updateDetails updateDetails(@Valid @RequestBody UpdateProfessionalDetailsDto dto, @AuthenticationPrincipal FleenUser user) {

  }
}
