package com.umulam.fleen.health.service.impl;

import com.umulam.fleen.health.constant.verification.ProfileVerificationStatus;
import com.umulam.fleen.health.constant.verification.VerificationDocumentType;
import com.umulam.fleen.health.exception.member.UserNotFoundException;
import com.umulam.fleen.health.model.domain.Business;
import com.umulam.fleen.health.model.domain.Member;
import com.umulam.fleen.health.model.domain.VerificationDocument;
import com.umulam.fleen.health.model.dto.business.UpdateBusinessDetailDto;
import com.umulam.fleen.health.model.dto.business.UploadBusinessDocumentDto;
import com.umulam.fleen.health.model.request.UpdateVerificationDocumentRequest;
import com.umulam.fleen.health.model.security.FleenUser;
import com.umulam.fleen.health.repository.jpa.BusinessJpaRepository;
import com.umulam.fleen.health.repository.jpa.VerificationDocumentJpaRepository;
import com.umulam.fleen.health.service.BusinessService;
import com.umulam.fleen.health.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
public class BusinessServiceImpl implements BusinessService {

  private final MemberService memberService;
  private final S3Service s3Service;
  private final BusinessJpaRepository repository;
  private final VerificationDocumentJpaRepository verificationDocumentJpaRepository;

  public BusinessServiceImpl(MemberService memberService,
                             S3Service s3Service,
                             BusinessJpaRepository repository,
                             VerificationDocumentJpaRepository verificationDocumentJpaRepository) {
    this.memberService = memberService;
    this.s3Service = s3Service;
    this.repository = repository;
    this.verificationDocumentJpaRepository = verificationDocumentJpaRepository;
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
    } else {
      business.setMember(member);
    }

    return repository.save(business);
  }

  @Override
  @Transactional
  public Object uploadDocuments(UploadBusinessDocumentDto dto, FleenUser user) {
    Member member = memberService.getMemberByEmailAddress(user.getEmailAddress());
    if (Objects.isNull(member)) {
      throw new UserNotFoundException(user.getEmailAddress());
    }

    List<VerificationDocument> existingDocuments = verificationDocumentJpaRepository.findVerificationDocumentByMember(member);
    List<VerificationDocument> newOrUpdatedDocument = setVerificationDocument(dto.toUpdateVerificationDocumentRequest(), existingDocuments);
    verificationDocumentJpaRepository.saveAll(newOrUpdatedDocument);
    return "Success";
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
      existingVerificationDocument.setFilename(verificationDocument.getFilename());
      existingVerificationDocument.setLink(verificationDocument.getLink());
      verificationDocumentMap.put(verificationDocument.getVerificationDocumentType(), existingVerificationDocument);
    }
    return new ArrayList<>(verificationDocumentMap.values());
  }

  @Override
  public Object requestForVerification(FleenUser user) {
    Member member = memberService.getMemberByEmailAddress(user.getEmailAddress());
    if (Objects.isNull(member)) {
      throw new UserNotFoundException(user.getEmailAddress());
    }
    member.setVerificationStatus(ProfileVerificationStatus.IN_PROGRESS);
    return "Success";
  }

  @Override
  public Object checkVerificationStatus() {
    return null;
  }
}
