package com.umulam.fleen.health.service;

import com.umulam.fleen.health.constant.verification.ProfileVerificationStatus;
import com.umulam.fleen.health.model.domain.Business;
import com.umulam.fleen.health.model.dto.business.UpdateBusinessDetailDto;
import com.umulam.fleen.health.model.dto.business.UploadBusinessDocumentDto;
import com.umulam.fleen.health.model.security.FleenUser;
import com.umulam.fleen.health.model.view.BusinessView;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BusinessService {

  List<Object> getBusinessesByAdmin();

  @Transactional
  Business updateDetails(UpdateBusinessDetailDto dto, FleenUser user);

  @Transactional
  void uploadDocuments(UploadBusinessDocumentDto dto, FleenUser user);

  ProfileVerificationStatus checkVerificationStatus(FleenUser user);

  @Transactional
  void requestForVerification(FleenUser user);

  @Transactional(readOnly = true)
  BusinessView toBusinessView(Business entry);

  @Transactional(readOnly = true)
  List<BusinessView> toBusinessViews(List<Business> entries);

  void setVerificationDocument(BusinessView businessView);
}
