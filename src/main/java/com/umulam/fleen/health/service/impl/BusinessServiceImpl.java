package com.umulam.fleen.health.service.impl;

import com.umulam.fleen.health.constant.verification.ProfileVerificationStatus;
import com.umulam.fleen.health.constant.verification.VerificationDocumentType;
import com.umulam.fleen.health.exception.member.UserNotFoundException;
import com.umulam.fleen.health.model.domain.Business;
import com.umulam.fleen.health.model.domain.Country;
import com.umulam.fleen.health.model.domain.Member;
import com.umulam.fleen.health.model.domain.VerificationDocument;
import com.umulam.fleen.health.model.dto.business.UpdateBusinessDetailDto;
import com.umulam.fleen.health.model.dto.business.UploadBusinessDocumentDto;
import com.umulam.fleen.health.model.mapper.BusinessMapper;
import com.umulam.fleen.health.model.mapper.VerificationDocumentMapper;
import com.umulam.fleen.health.model.request.UpdateVerificationDocumentRequest;
import com.umulam.fleen.health.model.security.FleenUser;
import com.umulam.fleen.health.model.view.BusinessView;
import com.umulam.fleen.health.model.view.VerificationDocumentView;
import com.umulam.fleen.health.repository.jpa.BusinessJpaRepository;
import com.umulam.fleen.health.service.BusinessService;
import com.umulam.fleen.health.service.CountryService;
import com.umulam.fleen.health.service.MemberService;
import com.umulam.fleen.health.service.VerificationDocumentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
public class BusinessServiceImpl implements BusinessService {

  private final MemberService memberService;
  private final S3Service s3Service;
  private final CountryService countryService;
  private final VerificationDocumentService verificationDocumentService;
  private final BusinessJpaRepository repository;

  public BusinessServiceImpl(MemberService memberService,
                             S3Service s3Service,
                             CountryService countryService,
                             VerificationDocumentService verificationDocumentService,
                             BusinessJpaRepository repository) {
    this.memberService = memberService;
    this.s3Service = s3Service;
    this.countryService = countryService;
    this.verificationDocumentService = verificationDocumentService;
    this.repository = repository;
  }

  @Override
  public List<BusinessView> toBusinessViews(List<Business> businesses) {
    return BusinessMapper.toBusinessViews(businesses);
  }

  @Override
  public BusinessView toBusinessView(Business business) {
    return BusinessMapper.toBusinessView(business);
  }

  @Override
  public List<Object> getBusinessesByAdmin() {
    return null;
  }

  @Override
  @Transactional
  public Business updateDetails(UpdateBusinessDetailDto dto, FleenUser user) {
    Business business = dto.toBusiness();
    Member member = memberService.getMemberByEmailAddress(user.getEmailAddress());
    if (Objects.isNull(member)) {
      throw new UserNotFoundException(user.getEmailAddress());
    }

    Optional<Business> businessExists = repository.findBusinessByEmailAddress(user.getEmailAddress());
    if (businessExists.isPresent()) {
      Business existingBusiness = businessExists.get();
      business.setId(existingBusiness.getId());
      business.setMember(member);
      business.setCountry(existingBusiness.getCountry());
    } else {
      Country country = countryService.getCountry(business.getCountry().getId());
      business.setMember(member);
      business.setCountry(country);
    }

    return repository.save(business);
  }

  @Override
  @Transactional
  public void uploadDocuments(UploadBusinessDocumentDto dto, FleenUser user) {
    Member member = memberService.getMemberByEmailAddress(user.getEmailAddress());
    if (Objects.isNull(member)) {
      throw new UserNotFoundException(user.getEmailAddress());
    }

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
    Member member = memberService.getMemberByEmailAddress(user.getEmailAddress());
    if (Objects.isNull(member)) {
      throw new UserNotFoundException(user.getEmailAddress());
    }
    member.setVerificationStatus(ProfileVerificationStatus.IN_PROGRESS);
    memberService.save(member);
  }

  @Override
  public ProfileVerificationStatus checkVerificationStatus(FleenUser user) {
    Member member = memberService.getMemberByEmailAddress(user.getEmailAddress());
    if (Objects.isNull(member)) {
      throw new UserNotFoundException(user.getEmailAddress());
    }
    return memberService.getVerificationStatus(user.getId());
  }

  @Override
  public void setVerificationDocument(BusinessView businessView) {
    String emailAddress = businessView.getMember().getEmailAddress();
    List<VerificationDocument> verificationDocuments = verificationDocumentService.getByMemberEmailAddress(emailAddress);
    List<VerificationDocumentView> verificationDocumentViews = VerificationDocumentMapper.toVerificationDocumentViews(verificationDocuments);
    businessView.setVerificationDocuments(verificationDocumentViews);
  }
}
