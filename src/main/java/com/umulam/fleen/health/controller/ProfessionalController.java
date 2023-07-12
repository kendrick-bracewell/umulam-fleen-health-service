package com.umulam.fleen.health.controller;

import com.umulam.fleen.health.constant.verification.ProfileVerificationStatus;
import com.umulam.fleen.health.model.domain.Professional;
import com.umulam.fleen.health.model.dto.professional.UpdateProfessionalDetailsDto;
import com.umulam.fleen.health.model.dto.professional.UploadProfessionalDocumentDto;
import com.umulam.fleen.health.model.response.FleenHealthResponse;
import com.umulam.fleen.health.model.security.FleenUser;
import com.umulam.fleen.health.model.view.ProfessionalView;
import com.umulam.fleen.health.model.view.UserVerificationStatusView;
import com.umulam.fleen.health.service.ProfessionalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.umulam.fleen.health.constant.base.FleenHealthConstant.REQUEST_FOR_VERIFICATION;
import static com.umulam.fleen.health.constant.base.FleenHealthConstant.VERIFICATION_DOCUMENT_UPDATED;

@Slf4j
@RestController
@RequestMapping(value = "professional")
public class ProfessionalController {

  private final ProfessionalService professionalService;

  public ProfessionalController(ProfessionalService professionalService) {
    this.professionalService = professionalService;
  }

  @PutMapping(value = "/verification/update-details")
  public ProfessionalView updateDetails(@Valid @RequestBody UpdateProfessionalDetailsDto dto, @AuthenticationPrincipal FleenUser user) {
    Professional professional = professionalService.updateDetails(dto, user);
    ProfessionalView professionalView = professionalService.toProfessionalView(professional);
    professionalService.setVerificationDocument(professionalView);
    return professionalView;
  }

  @PutMapping(value = "/verification/upload-documents")
  public FleenHealthResponse uploadDocuments(@Valid @RequestBody UploadProfessionalDocumentDto dto, @AuthenticationPrincipal FleenUser user) {
    professionalService.uploadDocuments(dto, user);
    return new FleenHealthResponse(VERIFICATION_DOCUMENT_UPDATED);
  }

  @PutMapping(value = "/request-verification")
  public FleenHealthResponse requestVerification(@AuthenticationPrincipal FleenUser user) {
    professionalService.requestForVerification(user);
    return new FleenHealthResponse(REQUEST_FOR_VERIFICATION);
  }

  @GetMapping(value = "/check-verification-status")
  public UserVerificationStatusView checkVerificationStatus(@AuthenticationPrincipal FleenUser user) {
    ProfileVerificationStatus status = professionalService.checkVerificationStatus(user);
    return new UserVerificationStatusView(status.name());
  }

  public void viewSessions() {

  }

  public void viewPatientSession() {

  }

  public void viewPatientDetail() {

  }
}
