package com.umulam.fleen.health.service.admin.impl;

import com.umulam.fleen.health.constant.verification.ProfileVerificationStatus;
import com.umulam.fleen.health.model.domain.Professional;
import com.umulam.fleen.health.model.mapper.ProfessionalMapper;
import com.umulam.fleen.health.model.request.search.ProfessionalSearchRequest;
import com.umulam.fleen.health.model.view.ProfessionalView;
import com.umulam.fleen.health.model.view.SearchResultView;
import com.umulam.fleen.health.repository.jpa.ProfessionalJpaRepository;
import com.umulam.fleen.health.service.CountryService;
import com.umulam.fleen.health.service.MemberService;
import com.umulam.fleen.health.service.VerificationDocumentService;
import com.umulam.fleen.health.service.admin.AdminProfessionalService;
import com.umulam.fleen.health.service.impl.ProfessionalServiceImpl;
import com.umulam.fleen.health.service.impl.S3Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.umulam.fleen.health.util.FleenHealthUtil.areNotEmpty;
import static com.umulam.fleen.health.util.FleenHealthUtil.toSearchResult;
import static java.util.Objects.nonNull;

@Slf4j
@Service
@Qualifier("adminProfessionalService")
public class AdminProfessionalServiceImpl extends ProfessionalServiceImpl implements AdminProfessionalService {

  public AdminProfessionalServiceImpl(MemberService memberService,
                                 S3Service s3Service,
                                 CountryService countryService,
                                 VerificationDocumentService verificationDocumentService,
                                 ProfessionalJpaRepository repository) {
    super(memberService, s3Service, countryService, verificationDocumentService, repository);
  }


  @Override
  @Transactional(readOnly = true)
  public SearchResultView findProfessionals(ProfessionalSearchRequest req) {
    Page<Professional> page;

    if (areNotEmpty(req.getStartDate(), req.getEndDate())) {
      page = repository.findByCreatedOnAndUpdatedOnBetween(req.getStartDate().atStartOfDay(), req.getEndDate().atStartOfDay(), req.getPage());
    } else if (areNotEmpty(req.getFirstName(), req.getLastName())) {
      page = repository.findByFirstNameAndLastName(req.getFirstName(), req.getLastName(), req.getPage());
    } else if (nonNull(req.getAvailabilityStatus())) {
      page = repository.findByAvailabilityStatus(req.getAvailabilityStatus(), req.getPage());
    } else if (nonNull(req.getProfessionalType())) {
      page = repository.findByProfessionalType(req.getProfessionalType(), req.getPage());
    } else if (nonNull(req.getQualificationType())) {
      page = repository.findByQualification(req.getQualificationType(), req.getPage());
    } else if (nonNull(req.getLanguageSpoken())) {
      page = repository.findByLanguageSpoken(req.getLanguageSpoken(), req.getPage());
    } else if (nonNull(req.getVerificationStatus())) {
      page = repository.findByVerificationStatus(req.getVerificationStatus(), req.getPage());
    } else if (nonNull(req.getBeforeDate())) {
      page = repository.findByCreatedOnBefore(req.getBeforeDate().atStartOfDay(), req.getPage());
    } else if (nonNull(req.getAfterDate())) {
      page = repository.findByCreatedOnBefore(req.getAfterDate().atStartOfDay(), req.getPage());
    } else {
      page = repository.findAll(req.getPage());
    }

    List<ProfessionalView> views = ProfessionalMapper.toProfessionalViews(page.getContent());
    return toSearchResult(views, page);
  }

  @Override
  @Transactional(readOnly = true)
  public SearchResultView findProfessionalsVerificationStatus(ProfessionalSearchRequest req) {
    Page<Professional> page;
    ProfileVerificationStatus verificationStatus = ProfileVerificationStatus.PENDING;
    if (areNotEmpty(req.getStartDate(), req.getEndDate())) {
      page = repository.findByVerificationStatusBetween(verificationStatus, req.getStartDate().atStartOfDay(), req.getEndDate().atStartOfDay(), req.getPage());
    } else if (areNotEmpty(req.getFirstName(), req.getLastName())) {
      page = repository.findByVerificationStatusAndFirstOrLastName(verificationStatus, req.getFirstName(), req.getLastName(), req.getPage());
    } else {
      page = repository.findByVerificationStatus(verificationStatus, req.getPage());
    }

    List<ProfessionalView> views = ProfessionalMapper.toProfessionalViews(page.getContent());
    return toSearchResult(views, page);
  }


}
