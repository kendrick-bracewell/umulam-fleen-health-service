package com.umulam.fleen.health.service;

import com.umulam.fleen.health.constant.verification.ProfileVerificationStatus;
import com.umulam.fleen.health.model.domain.Member;
import com.umulam.fleen.health.model.domain.Professional;
import com.umulam.fleen.health.model.dto.professional.UpdateProfessionalAvailabilityDto;
import com.umulam.fleen.health.model.dto.professional.UpdateProfessionalAvailabilityStatusDto;
import com.umulam.fleen.health.model.dto.professional.UpdateProfessionalDetailsDto;
import com.umulam.fleen.health.model.dto.professional.UploadProfessionalDocumentDto;
import com.umulam.fleen.health.model.response.professional.GetProfessionalUpdateAvailabilityStatusResponse;
import com.umulam.fleen.health.model.response.professional.GetProfessionalUpdateVerificationDetailResponse;
import com.umulam.fleen.health.model.security.FleenUser;
import com.umulam.fleen.health.model.view.ProfessionalAvailabilityView;
import com.umulam.fleen.health.model.view.VerificationDocumentView;
import com.umulam.fleen.health.model.view.professional.ProfessionalView;
import com.umulam.fleen.health.model.view.professional.ProfessionalViewBasic;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ProfessionalService {

  @Transactional(readOnly = true)
  ProfessionalView findProfessionalById(Long id);

  @Transactional(readOnly = true)
  Professional getDetails(FleenUser user);

  @Transactional
  Professional updateDetails(UpdateProfessionalDetailsDto dto, FleenUser user);

  @Transactional
  List<VerificationDocumentView> getUploadDocuments(FleenUser user);

  @Transactional
  void uploadDocuments(UploadProfessionalDocumentDto dto, FleenUser user);

  ProfileVerificationStatus checkVerificationStatus(FleenUser user);

  @Transactional
  void requestForVerification(FleenUser user);

  @Transactional(readOnly = true)
  ProfessionalView toProfessionalView(Professional entry);

  @Transactional(readOnly = true)
  List<ProfessionalView> toProfessionalViews(List<Professional> entries);

  void setVerificationDocument(ProfessionalView professionalViewView);

  GetProfessionalUpdateAvailabilityStatusResponse getProfessionalAvailabilityStatus(FleenUser user);

  @Transactional
  void updateAvailabilityStatus(UpdateProfessionalAvailabilityStatusDto dto, FleenUser user);

  @Transactional(readOnly = true)
  ProfessionalViewBasic findProfessionalBasicById(Long id);

  @Transactional
  Professional save(Professional professional);

  Professional getProfessional(Long id);

  GetProfessionalUpdateVerificationDetailResponse getUpdateVerificationDetail(FleenUser user);

  List<ProfessionalAvailabilityView> getUpdateAvailabilityOrSchedule(FleenUser user);

  void updateAvailabilityOrSchedule(UpdateProfessionalAvailabilityDto dto, FleenUser user);

  Optional<Professional> findProfessionalByMember(Member member);

  List<Professional> findProfessionalsById(List<Long> ids);

  Double getProfessionalPrice(Long memberId);
}
