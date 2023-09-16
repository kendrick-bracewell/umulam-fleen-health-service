package com.umulam.fleen.health.controller;

import com.amazonaws.services.cognitoidp.model.UserNotFoundException;
import com.umulam.fleen.health.constant.verification.ProfileVerificationStatus;
import com.umulam.fleen.health.exception.authentication.InvalidAuthenticationException;
import com.umulam.fleen.health.exception.authentication.InvalidAuthenticationToken;
import com.umulam.fleen.health.exception.base.FleenHealthException;
import com.umulam.fleen.health.exception.professional.HasNoProfessionalProfileException;
import com.umulam.fleen.health.model.domain.Professional;
import com.umulam.fleen.health.model.dto.professional.UpdateProfessionalAvailabilityDto;
import com.umulam.fleen.health.model.dto.professional.UpdateProfessionalAvailabilityStatusDto;
import com.umulam.fleen.health.model.dto.professional.UpdateProfessionalDetailsDto;
import com.umulam.fleen.health.model.dto.professional.UploadProfessionalDocumentDto;
import com.umulam.fleen.health.model.response.FleenHealthResponse;
import com.umulam.fleen.health.model.response.professional.GetProfessionalUpdateAvailabilityStatusResponse;
import com.umulam.fleen.health.model.response.professional.GetProfessionalUpdateVerificationDetailResponse;
import com.umulam.fleen.health.model.security.FleenUser;
import com.umulam.fleen.health.model.view.ProfessionalAvailabilityView;
import com.umulam.fleen.health.model.view.UserVerificationStatusView;
import com.umulam.fleen.health.model.view.VerificationDocumentView;
import com.umulam.fleen.health.model.view.professional.ProfessionalView;
import com.umulam.fleen.health.service.ProfessionalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.umulam.fleen.health.constant.base.FleenHealthConstant.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping(value = "professional")
@Tag(name = "Professional", description = "Professional User")
public class ProfessionalController {

  private final ProfessionalService service;

  public ProfessionalController(ProfessionalService service) {
    this.service = service;
  }

  @GetMapping(value = "/get-details")
  @Operation(summary = "Retrieves professional details for the authenticated user.")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Successfully retrieved professional details",
      content = { @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProfessionalView.class)) }),
    @ApiResponse(responseCode = "400", description = "User not found",
      content = {
        @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserNotFoundException.class) ),
        @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = HasNoProfessionalProfileException.class) )
    }),
    @ApiResponse(responseCode = "401", description = "Unauthorized - User not authenticated",
      content = {
      @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = InvalidAuthenticationException.class) ),
      @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = InvalidAuthenticationToken.class) )
    }),
    @ApiResponse(responseCode = "500", description = "Internal Server Error",
      content = { @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = FleenHealthException.class) ) })
  })
  public ProfessionalView getDetails(@Parameter(description = "Authenticated user as a Professional", required = true)
                                     @AuthenticationPrincipal FleenUser user) {
    Professional professional = service.getDetails(user);
    ProfessionalView professionalView = service.toProfessionalView(professional);
    service.setVerificationDocument(professionalView);
    return professionalView;
  }
  

  @Operation(summary = "Retrieves professional details for the authenticated user for update.")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Successfully retrieved professional details for update",
      content = {
        @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = GetProfessionalUpdateVerificationDetailResponse.class)) }),
    @ApiResponse(responseCode = "400", description = "User not found",
      content = { @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserNotFoundException.class) ) }),
    @ApiResponse(responseCode = "401", description = "Unauthorized - User not authenticated",
      content = {
        @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = InvalidAuthenticationException.class) ),
        @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = InvalidAuthenticationToken.class) )
      }),
    @ApiResponse(responseCode = "500", description = "Internal Server Error",
      content = { @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = FleenHealthException.class) )} )
  })
  @GetMapping(value = "/verification/update-details")
  public GetProfessionalUpdateVerificationDetailResponse getUpdateVerificationDetails(@AuthenticationPrincipal FleenUser user) {
    return service.getUpdateVerificationDetail(user);
  }

  @PutMapping(value = "/verification/update-details")
  public ProfessionalView updateDetails(@Valid @RequestBody UpdateProfessionalDetailsDto dto, @AuthenticationPrincipal FleenUser user) {
    Professional professional = service.updateDetails(dto, user);

    ProfessionalView professionalView = service.toProfessionalView(professional);
    service.setVerificationDocument(professionalView);
    return professionalView;
  }

  @GetMapping(value = "/verification/upload-documents")
  public List<VerificationDocumentView> getUpdateVerificationDocuments(@AuthenticationPrincipal FleenUser user) {
    return service.getUploadDocuments(user);
  }

  @PutMapping(value = "/verification/upload-document")
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
    return new UserVerificationStatusView(status.name(), status.getValue());
  }

  @GetMapping(value = "/check-availability-status")
  public GetProfessionalUpdateAvailabilityStatusResponse getUpdateAvailabilityStatus(@AuthenticationPrincipal FleenUser user) {
    return service.getProfessionalAvailabilityStatus(user);
  }

  @PutMapping(value = "/update-availability-status")
  public FleenHealthResponse updateAvailabilityStatus(@Valid @RequestBody UpdateProfessionalAvailabilityStatusDto dto,
                                                      @AuthenticationPrincipal FleenUser user) {
    service.updateAvailabilityStatus(dto, user);
    return new FleenHealthResponse(AVAILABILITY_STATUS_UPDATED);
  }

  @GetMapping(value = "/update-availability")
  public List<ProfessionalAvailabilityView> getUpdateAvailabilityOrSchedule(@AuthenticationPrincipal FleenUser user) {
    return service.getUpdateAvailabilityOrSchedule(user);
  }

  @PutMapping(value = "/update-availability")
  public FleenHealthResponse updateAvailabilityOrSchedule(@Valid @RequestBody UpdateProfessionalAvailabilityDto dto,
                                             @AuthenticationPrincipal FleenUser user) {
    service.updateAvailabilityOrSchedule(dto, user);
    return new FleenHealthResponse(PROFESSIONAL_AVAILABILITY_OR_SCHEDULED_UPDATED);
  }

}
