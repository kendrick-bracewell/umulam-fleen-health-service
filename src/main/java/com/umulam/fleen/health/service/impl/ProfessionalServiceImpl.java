package com.umulam.fleen.health.service.impl;

import com.umulam.fleen.health.constant.verification.ProfileVerificationStatus;
import com.umulam.fleen.health.constant.verification.VerificationDocumentType;
import com.umulam.fleen.health.exception.member.UserNotFoundException;
import com.umulam.fleen.health.model.domain.Country;
import com.umulam.fleen.health.model.domain.Member;
import com.umulam.fleen.health.model.domain.Professional;
import com.umulam.fleen.health.model.domain.VerificationDocument;
import com.umulam.fleen.health.model.dto.professional.UpdateProfessionalDetailsDto;
import com.umulam.fleen.health.model.dto.professional.UploadProfessionalDocumentDto;
import com.umulam.fleen.health.model.mapper.ProfessionalMapper;
import com.umulam.fleen.health.model.mapper.VerificationDocumentMapper;
import com.umulam.fleen.health.model.request.UpdateVerificationDocumentRequest;
import com.umulam.fleen.health.model.security.FleenUser;
import com.umulam.fleen.health.model.view.ProfessionalView;
import com.umulam.fleen.health.model.view.VerificationDocumentView;
import com.umulam.fleen.health.repository.jpa.ProfessionalJpaRepository;
import com.umulam.fleen.health.service.CountryService;
import com.umulam.fleen.health.service.MemberService;
import com.umulam.fleen.health.service.ProfessionalService;
import com.umulam.fleen.health.service.VerificationDocumentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
public class ProfessionalServiceImpl implements ProfessionalService {


  private final MemberService memberService;
  private final S3Service s3Service;
  private final CountryService countryService;
  private final VerificationDocumentService verificationDocumentService;
  private final ProfessionalJpaRepository repository;

  public ProfessionalServiceImpl(MemberService memberService,
                             S3Service s3Service,
                             CountryService countryService,
                             VerificationDocumentService verificationDocumentService,
                             ProfessionalJpaRepository repository) {
    this.memberService = memberService;
    this.s3Service = s3Service;
    this.countryService = countryService;
    this.verificationDocumentService = verificationDocumentService;
    this.repository = repository;
  }

  @Override
  public List<ProfessionalView> toProfessionalViews(List<Professional> businesses) {
    return ProfessionalMapper.toProfessionalViews(businesses);
  }

  @Override
  public ProfessionalView toProfessionalView(Professional business) {
    return ProfessionalMapper.toProfessionalView(business);
  }

  @Override
  public List<Object> getProfessionalsByAdmin() {
    return null;
  }

  @Override
  @Transactional
  public Professional updateDetails(UpdateProfessionalDetailsDto dto, FleenUser user) {
    Professional business = dto.toProfessional();
    Member member = memberService.getMemberByEmailAddress(user.getEmailAddress());
    if (Objects.isNull(member)) {
      throw new UserNotFoundException(user.getEmailAddress());
    }

    Country country;
    Optional<Professional> businessExists = repository.findProfessionalByEmailAddress(user.getEmailAddress());
    if (businessExists.isPresent()) {
      Professional existingProfessional = businessExists.get();
      country = existingProfessional.getCountry();
      business.setId(existingProfessional.getId());
    } else {
      country = countryService.getCountry(business.getCountry().getId());
    }

    business.setMember(member);
    business.getMember().setMemberStatus(member.getMemberStatus());
    business.setCountry(country);

    return repository.save(business);
  }

  @Override
  @Transactional
  public void uploadDocuments(UploadProfessionalDocumentDto dto, FleenUser user) {
    Member member = getMember(user.getEmailAddress());

    List<VerificationDocument> existingDocuments = verificationDocumentService.getVerificationDocumentsByMember(member);
    List<VerificationDocument> newOrUpdatedDocument = setVerificationDocument(dto.toUpdateVerificationDocumentRequest(), existingDocuments);
    newOrUpdatedDocument.forEach(document -> document.setMember(member));

    verificationDocumentService.saveMany(newOrUpdatedDocument);
  }

  private List<VerificationDocument> setVerificationDocument(List<UpdateVerificationDocumentRequest> updateVerificationDocumentRequest,
                                                             List<VerificationDocument> existingVerificationDocuments) {
    Map<VerificationDocumentType, VerificationDocument> verificationDocumentMap = new HashMap<>();
    for (UpdateVerificationDocumentRequest request: updateVerificationDocumentRequest) {
      if (Objects.nonNull(request.getDocumentLink())) {
        VerificationDocument verificationDocument = VerificationDocument.builder()
                .verificationDocumentType(request.getVerificationDocumentType())
                .filename(s3Service.getObjectKeyFromUrl(request.getDocumentLink()))
                .link(request.getDocumentLink())
                .build();
        verificationDocumentMap.put(request.getVerificationDocumentType(), verificationDocument);
      }
    }

    for (VerificationDocument existingVerificationDocument: existingVerificationDocuments) {
      VerificationDocument verificationDocument = verificationDocumentMap.get(existingVerificationDocument.getVerificationDocumentType());
      if (Objects.nonNull(verificationDocument)) {
        existingVerificationDocument.setFilename(verificationDocument.getFilename());
        existingVerificationDocument.setLink(verificationDocument.getLink());
        verificationDocumentMap.put(verificationDocument.getVerificationDocumentType(), existingVerificationDocument);
      }
    }
    return new ArrayList<>(verificationDocumentMap.values());
  }

  @Override
  @Transactional
  public void requestForVerification(FleenUser user) {
    Member member = getMember(user.getEmailAddress());
    if (member.getVerificationStatus() == ProfileVerificationStatus.IN_PROGRESS) {
      return;
    }
    member.setVerificationStatus(ProfileVerificationStatus.IN_PROGRESS);
    memberService.save(member);
  }

  @Override
  public ProfileVerificationStatus checkVerificationStatus(FleenUser user) {
    Member member = getMember(user.getEmailAddress());
    return memberService.getVerificationStatus(user.getId());
  }

  @Override
  public void setVerificationDocument(ProfessionalView professionalView) {
    String emailAddress = professionalView.getMember().getEmailAddress();
    List<VerificationDocument> verificationDocuments = verificationDocumentService.getByMemberEmailAddress(emailAddress);
    List<VerificationDocumentView> verificationDocumentViews = VerificationDocumentMapper.toVerificationDocumentViews(verificationDocuments);
    professionalView.setVerificationDocuments(verificationDocumentViews);
  }

  private Member getMember(String emailAddress) {
    Member member = memberService.getMemberByEmailAddress(emailAddress);
    if (Objects.isNull(member)) {
      throw new UserNotFoundException(emailAddress);
    }
    return member;
  }

}
