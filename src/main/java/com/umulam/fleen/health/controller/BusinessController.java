package com.umulam.fleen.health.controller;

import com.umulam.fleen.health.model.domain.Business;
import com.umulam.fleen.health.model.domain.Country;
import com.umulam.fleen.health.model.dto.business.UpdateBusinessDetailDto;
import com.umulam.fleen.health.model.dto.business.UploadBusinessDocumentDto;
import com.umulam.fleen.health.model.mapper.BusinessMapper;
import com.umulam.fleen.health.model.response.FleenHealthResponse;
import com.umulam.fleen.health.model.security.FleenUser;
import com.umulam.fleen.health.model.view.BusinessView;
import com.umulam.fleen.health.service.BusinessService;
import com.umulam.fleen.health.service.CountryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.umulam.fleen.health.constant.base.FleenHealthConstant.VERIFICATION_DOCUMENT_UPDATED;

@Slf4j
@RestController
@RequestMapping(value = "business")
public class BusinessController {

  private final BusinessService businessService;

  public BusinessController(BusinessService businessService) {

    this.businessService = businessService;
  }

  @PutMapping(value = "/verification/update-details")
  public BusinessView updateDetails(@Valid @RequestBody UpdateBusinessDetailDto dto, @AuthenticationPrincipal FleenUser user) {
    Business business = businessService.updateDetails(dto, user);
    BusinessView businessView = businessService.toBusinessView(business);
    businessService.setVerificationDocument(businessView);
    return businessView;
  }

  @PutMapping(value = "/verification/upload-documents")
  public Object uploadDocuments(@Valid @RequestBody UploadBusinessDocumentDto dto, @AuthenticationPrincipal FleenUser user) {
    businessService.uploadDocuments(dto, user);
    return new FleenHealthResponse(VERIFICATION_DOCUMENT_UPDATED);
  }

  @PutMapping(value = "/request-verification")
  public Object requestVerification() {
    return null;
  }
}
