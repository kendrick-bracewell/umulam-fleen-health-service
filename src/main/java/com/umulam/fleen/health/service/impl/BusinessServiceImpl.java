package com.umulam.fleen.health.service.impl;

import com.umulam.fleen.health.constant.verification.ProfileVerificationStatus;
import com.umulam.fleen.health.exception.member.UserNotFoundException;
import com.umulam.fleen.health.model.domain.Business;
import com.umulam.fleen.health.model.domain.Country;
import com.umulam.fleen.health.model.domain.Member;
import com.umulam.fleen.health.model.domain.VerificationDocument;
import com.umulam.fleen.health.model.dto.business.UpdateBusinessDetailDto;
import com.umulam.fleen.health.model.dto.business.UploadBusinessDocumentDto;
import com.umulam.fleen.health.model.mapper.BusinessMapper;
import com.umulam.fleen.health.model.mapper.VerificationDocumentMapper;
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

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class BusinessServiceImpl implements BusinessService, ProfileService {

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
    Member member = getMember(user.getEmailAddress());

    Country country;
    Optional<Business> businessExists = repository.findBusinessByEmailAddress(user.getEmailAddress());
    if (businessExists.isPresent()) {
      Business existingBusiness = businessExists.get();
      country = existingBusiness.getCountry();
      business.setId(existingBusiness.getId());
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
  public void uploadDocuments(UploadBusinessDocumentDto dto, FleenUser user) {
    saveVerificationDocument(user, dto.toUpdateVerificationDocumentRequest());
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
    getMember(user.getEmailAddress());
    return memberService.getVerificationStatus(user.getId());
  }

  @Override
  public void setVerificationDocument(BusinessView businessView) {
    String emailAddress = businessView.getMember().getEmailAddress();
    List<VerificationDocument> verificationDocuments = verificationDocumentService.getByMemberEmailAddress(emailAddress);
    List<VerificationDocumentView> verificationDocumentViews = VerificationDocumentMapper.toVerificationDocumentViews(verificationDocuments);
    businessView.setVerificationDocuments(verificationDocumentViews);
  }

  private Member getMember(String emailAddress) {
    Member member = memberService.getMemberByEmailAddress(emailAddress);
    if (Objects.isNull(member)) {
      throw new UserNotFoundException(emailAddress);
    }
    return member;
  }

  @Override
  public S3Service getS3Service() {
    return s3Service;
  }

  @Override
  public MemberService getMemberService() {
    return memberService;
  }

  @Override
  public VerificationDocumentService getVerificationDocumentService() {
    return verificationDocumentService;
  }
}
