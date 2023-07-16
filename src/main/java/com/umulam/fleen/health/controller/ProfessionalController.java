package com.umulam.fleen.health.controller;

import com.umulam.fleen.health.constant.verification.ProfileVerificationStatus;
import com.umulam.fleen.health.model.domain.Professional;
import com.umulam.fleen.health.model.dto.professional.UpdateProfessionalAvailabilityStatusDto;
import com.umulam.fleen.health.model.dto.professional.UpdateProfessionalDetailsDto;
import com.umulam.fleen.health.model.dto.professional.UploadProfessionalDocumentDto;
import com.umulam.fleen.health.model.response.FleenHealthResponse;
import com.umulam.fleen.health.model.response.professional.GetProfessionalUpdateAvailabilityStatusResponse;
import com.umulam.fleen.health.model.response.professional.GetUpdateVerificationDetailResponse;
import com.umulam.fleen.health.model.security.FleenUser;
import com.umulam.fleen.health.model.view.ProfessionalView;
import com.umulam.fleen.health.model.view.UserVerificationStatusView;
import com.umulam.fleen.health.service.ProfessionalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.umulam.fleen.health.constant.base.FleenHealthConstant.*;

@Slf4j
@RestController
@RequestMapping(value = "professional")
public class ProfessionalController {

  private final ProfessionalService service;

  public ProfessionalController(ProfessionalService service) {
    this.service = service;
  }

  @GetMapping(value = "/verification/update-details")
  public GetUpdateVerificationDetailResponse getUpdateVerificationDetails() {
    return service.getUpdateVerificationDetail();
  }

  @PutMapping(value = "/verification/update-details")
  public ProfessionalView updateDetails(@Valid @RequestBody UpdateProfessionalDetailsDto dto, @AuthenticationPrincipal FleenUser user) {
    Professional professional = service.updateDetails(dto, user);
    ProfessionalView professionalView = service.toProfessionalView(professional);
    service.setVerificationDocument(professionalView);
    return professionalView;
  }

  @PutMapping(value = "/verification/upload-documents")
  public FleenHealthResponse uploadDocuments(@Valid @RequestBody UploadProfessionalDocumentDto dto, @AuthenticationPrincipal FleenUser user) {
    service.uploadDocuments(dto, user);
    return new FleenHealthResponse(VERIFICATION_DOCUMENT_UPDATED);
  }

  @PutMapping(value = "/request-verification")
  public FleenHealthResponse requestVerification(@AuthenticationPrincipal FleenUser user) {
    service.requestForVerification(user);
    return new FleenHealthResponse(REQUEST_FOR_VERIFICATION);
  }

  @GetMapping(value = "/check-verification-status")
  public UserVerificationStatusView checkVerificationStatus(@AuthenticationPrincipal FleenUser user) {
    ProfileVerificationStatus status = service.checkVerificationStatus(user);
    return new UserVerificationStatusView(status.name());
  }

  @GetMapping(value = "/update-availability-status")
  public GetProfessionalUpdateAvailabilityStatusResponse getUpdateAvailabilityStatus(@AuthenticationPrincipal FleenUser user) {
    return service.getProfessionalAvailabilityStatus(user);
  }

  @PutMapping(value = "/update-availability-status")
  public FleenHealthResponse updateAvailabilityStatus(@Valid @RequestBody UpdateProfessionalAvailabilityStatusDto dto,
                                                      @AuthenticationPrincipal FleenUser user) {
    service.updateAvailabilityStatus(dto, user);
    return new FleenHealthResponse(AVAILABILITY_STATUS_UPDATED);
  }

  public void viewSessions() {

  }

  public void viewPatientSession() {

  }

  public void viewPatientDetail() {

  }
}
