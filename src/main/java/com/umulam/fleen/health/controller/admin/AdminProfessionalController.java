package com.umulam.fleen.health.controller.admin;

import com.umulam.fleen.health.model.dto.professional.UpdateProfessionalDetailsDto;
import com.umulam.fleen.health.model.dto.verification.UpdateProfileVerificationStatusDto;
import com.umulam.fleen.health.model.request.search.ProfessionalSearchRequest;
import com.umulam.fleen.health.model.response.FleenHealthResponse;
import com.umulam.fleen.health.model.view.ProfessionalView;
import com.umulam.fleen.health.model.view.search.SearchResultView;
import com.umulam.fleen.health.resolver.SearchParam;
import com.umulam.fleen.health.service.admin.AdminProfessionalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.umulam.fleen.health.constant.base.FleenHealthConstant.SUCCESS_MESSAGE;

@Slf4j
@RestController
@RequestMapping(value = "admin/professional")
public class AdminProfessionalController {

  private final AdminProfessionalService service;

  public AdminProfessionalController(
          @Qualifier("adminProfessionalService") AdminProfessionalService service) {
    this.service = service;
  }

  @GetMapping(value = "/entries")
  public SearchResultView findProfessionals(@SearchParam ProfessionalSearchRequest request) {
    return service.findProfessionals(request);
  }

  @GetMapping(value = "/entries/pending-verification")
  public SearchResultView findProfessionalsPendingVerification(@SearchParam ProfessionalSearchRequest request) {
    return service.findProfessionalsByVerificationStatus(request);
  }

  @GetMapping(value = "/detail/{id}")
  public ProfessionalView findProfessionalDetail(@PathVariable(name = "id") Integer professionalId) {
    return service.findProfessionalById(professionalId);
  }

  @PutMapping(value = "/update/{id}")
  public ProfessionalView updateProfessionalDetail(
          @PathVariable(name = "id") Integer professionalId,
          @Valid @RequestBody UpdateProfessionalDetailsDto dto) {
    return service.updateProfessionalDetail(dto, professionalId);
  }

  @PutMapping(value = "/update-verification-status/{id}")
  public FleenHealthResponse updateProfessionalVerificationStatus(
          @Valid @RequestBody UpdateProfileVerificationStatusDto dto,
          @PathVariable(name = "id") Integer professionalId) {
    service.updateProfessionalVerificationStatus(dto, professionalId);
    return new FleenHealthResponse(SUCCESS_MESSAGE);
  }

}
