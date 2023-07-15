package com.umulam.fleen.health.service.admin.impl;

import com.umulam.fleen.health.configuration.aws.s3.S3BucketNames;
import com.umulam.fleen.health.model.domain.Member;
import com.umulam.fleen.health.model.mapper.MemberMapper;
import com.umulam.fleen.health.model.request.MemberSearchRequest;
import com.umulam.fleen.health.model.view.MemberView;
import com.umulam.fleen.health.model.view.SearchResultView;
import com.umulam.fleen.health.repository.jpa.MemberJpaRepository;
import com.umulam.fleen.health.service.*;
import com.umulam.fleen.health.service.admin.AdminMemberService;
import com.umulam.fleen.health.service.external.aws.EmailServiceImpl;
import com.umulam.fleen.health.service.external.aws.MobileTextService;
import com.umulam.fleen.health.service.impl.CacheService;
import com.umulam.fleen.health.service.impl.MemberServiceImpl;
import com.umulam.fleen.health.service.impl.S3Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.umulam.fleen.health.util.FleenHealthUtil.areNotEmpty;
import static com.umulam.fleen.health.util.FleenHealthUtil.toSearchResult;
import static java.util.Objects.nonNull;

@Slf4j
@Service
@Qualifier("adminMemberService")
public class AdminMemberServiceImpl extends MemberServiceImpl implements AdminMemberService {

  private final MemberJpaRepository repository;

  public AdminMemberServiceImpl(MemberJpaRepository repository,
                                MfaService mfaService,
                                @Lazy AuthenticationService authenticationService,
                                CacheService cacheService,
                                MobileTextService mobileTextService,
                                EmailServiceImpl emailService,
                                S3Service s3Service,
                                MemberStatusService memberStatusService,
                                RoleService roleService,
                                S3BucketNames bucketNames,
                                PasswordEncoder passwordEncoder) {
    super(repository, mfaService, authenticationService, cacheService, mobileTextService,
          emailService, s3Service, memberStatusService, roleService, bucketNames, passwordEncoder);
    this.repository = repository;
  }

  @Override
  @Transactional(readOnly = true)
  public SearchResultView findMembers(MemberSearchRequest req) {
    Page<Member> page;

    if (areNotEmpty(req.getStartDate(), req.getEndDate())) {
      page = repository.findByCreatedOnAndUpdatedOnBetween(req.getStartDate().atStartOfDay(), req.getEndDate().atStartOfDay(), req.getPage());
    } else if (areNotEmpty(req.getFirstName(), req.getLastName())) {
      page = repository.findByFirstNameAndLastName(req.getFirstName(), req.getLastName(), req.getPage());
    } else if (nonNull(req.getEmailAddress())) {
      page = repository.findByEmailAddress(req.getEmailAddress(), req.getPage());
    } else if (nonNull(req.getVerificationStatus())) {
      page = repository.findByVerificationStatus(req.getVerificationStatus(), req.getPage());
    } else if (nonNull(req.getBeforeDate())) {
      page = repository.findByCreatedOnBefore(req.getBeforeDate().atStartOfDay(), req.getPage());
    } else if (nonNull(req.getAfterDate())) {
      page = repository.findByCreatedOnBefore(req.getAfterDate().atStartOfDay(), req.getPage());
    } else {
      page = repository.findAll(req.getPage());
    }

    List<MemberView> views = MemberMapper.toMemberViews(page.getContent());
    return toSearchResult(views, page);
  }


  @Override
  public ProfileVerificationMessageService getProfileVerificationMessageService() {
    return null;
  }

  @Override
  public VerificationHistoryService getVerificationHistoryService() {
    return null;
  }
}
