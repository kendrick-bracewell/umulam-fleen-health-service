package com.umulam.fleen.health.service.admin.impl;

import com.umulam.fleen.health.exception.healthsession.HealthSessionNotFoundException;
import com.umulam.fleen.health.model.domain.HealthSession;
import com.umulam.fleen.health.model.domain.HealthSessionReview;
import com.umulam.fleen.health.model.request.search.HealthSessionSearchRequest;
import com.umulam.fleen.health.model.request.search.base.SearchRequest;
import com.umulam.fleen.health.model.view.healthsession.HealthSessionReviewView;
import com.umulam.fleen.health.model.view.healthsession.HealthSessionView;
import com.umulam.fleen.health.model.view.search.SearchResultView;
import com.umulam.fleen.health.repository.jpa.HealthSessionJpaRepository;
import com.umulam.fleen.health.repository.jpa.HealthSessionProfessionalJpaRepository;
import com.umulam.fleen.health.repository.jpa.HealthSessionReviewJpaRepository;
import com.umulam.fleen.health.repository.jpa.ProfessionalAvailabilityJpaRepository;
import com.umulam.fleen.health.repository.jpa.admin.AdminHealthSessionJpaRepository;
import com.umulam.fleen.health.repository.jpa.transaction.TransactionJpaRepository;
import com.umulam.fleen.health.service.ExchangeRateService;
import com.umulam.fleen.health.service.MemberService;
import com.umulam.fleen.health.service.ProfessionalService;
import com.umulam.fleen.health.service.admin.AdminHealthSessionService;
import com.umulam.fleen.health.service.impl.ConfigService;
import com.umulam.fleen.health.service.impl.FleenHealthEventService;
import com.umulam.fleen.health.service.session.impl.HealthSessionServiceImpl;
import com.umulam.fleen.health.util.UniqueReferenceGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.umulam.fleen.health.model.mapper.HealthSessionMapper.toHealthSessionView;
import static com.umulam.fleen.health.model.mapper.HealthSessionMapper.toHealthSessionViews;
import static com.umulam.fleen.health.model.mapper.HealthSessionReviewMapper.toHealthSessionReviewViews;
import static com.umulam.fleen.health.util.FleenHealthUtil.areNotEmpty;
import static com.umulam.fleen.health.util.FleenHealthUtil.toSearchResult;
import static java.util.Objects.nonNull;

@Slf4j
@Service
@Qualifier("adminHealthSessionService")
public class AdminHealthSessionServiceImpl extends HealthSessionServiceImpl implements AdminHealthSessionService {

  private final AdminHealthSessionJpaRepository adminHealthSessionJpaRepository;
  private final HealthSessionJpaRepository healthSessionJpaRepository;
  private final HealthSessionReviewJpaRepository healthSessionReviewJpaRepository;

  public AdminHealthSessionServiceImpl(AdminHealthSessionJpaRepository adminHealthSessionJpaRepository,
                                       HealthSessionJpaRepository healthSessionJpaRepository,
                                       HealthSessionReviewJpaRepository healthSessionReviewJpaRepository,
                                       FleenHealthEventService eventService,
                                       HealthSessionProfessionalJpaRepository sessionProfessionalJpaRepository,
                                       ProfessionalService professionalService,
                                       UniqueReferenceGenerator referenceGenerator,
                                       TransactionJpaRepository transactionJpaRepository,
                                       ProfessionalAvailabilityJpaRepository professionalAvailabilityJpaRepository,
                                       MemberService memberService,
                                       ExchangeRateService exchangeRateService,
                                       ConfigService configService) {
    super(sessionProfessionalJpaRepository, healthSessionJpaRepository, professionalService, referenceGenerator, transactionJpaRepository,
      professionalAvailabilityJpaRepository, healthSessionReviewJpaRepository, eventService, memberService, exchangeRateService, configService);
    this.adminHealthSessionJpaRepository = adminHealthSessionJpaRepository;
    this.healthSessionJpaRepository = healthSessionJpaRepository;
    this.healthSessionReviewJpaRepository = healthSessionReviewJpaRepository;
  }

  @Override
  public SearchResultView viewSessions(HealthSessionSearchRequest req) {
    Page<HealthSession> page;

    if (areNotEmpty(req.getStartDate(), req.getEndDate())) {
      page = adminHealthSessionJpaRepository.findByDateBetween(req.getStartDate().atStartOfDay(), req.getEndDate().atStartOfDay(), req.getPage());
    } else if (areNotEmpty(req.getReference())) {
      page = adminHealthSessionJpaRepository.findByReference(req.getReference(), req.getPage());
    } else if (nonNull(req.getLocation())) {
      page = adminHealthSessionJpaRepository.findByLocation(req.getLocation(), req.getPage());
    } else if (nonNull(req.getTimezone())) {
      page = adminHealthSessionJpaRepository.findByTimezone(req.getTimezone(), req.getPage());
    } else if (nonNull(req.getDate())) {
      page = adminHealthSessionJpaRepository.findByDate(req.getDate(), req.getPage());
    } else if (nonNull(req.getTime())) {
      page = adminHealthSessionJpaRepository.findByTime(req.getTime(), req.getPage());
    } else if (nonNull(req.getSessionStatus())) {
      page = adminHealthSessionJpaRepository.findByStatus(req.getSessionStatus(), req.getPage());
    } else {
      page = adminHealthSessionJpaRepository.findAll(req.getPage());
    }

    List<HealthSessionView> views = toHealthSessionViews(page.getContent());
    return toSearchResult(views, page);
  }

  @Override
  public HealthSessionView viewSession(Integer healthSessionId) {
    Optional<HealthSession> existingHealthSession = healthSessionJpaRepository.findById(healthSessionId);
    if (existingHealthSession.isPresent()) {
      return toHealthSessionView(existingHealthSession.get());
    }
    throw new HealthSessionNotFoundException(healthSessionId);
  }

  @Override
  public SearchResultView viewSessionReviews(SearchRequest req) {
    Page<HealthSessionReview> page = healthSessionReviewJpaRepository.findAll(req.getPage());
    List<HealthSessionReviewView> views = toHealthSessionReviewViews(page.getContent());
    return toSearchResult(views, page);
  }

  @Override
  @Transactional
  public void cancelSession(Integer healthSessionId) {
    Optional<HealthSession> healthSessionExist = healthSessionJpaRepository.findById(healthSessionId);
    cancelSession(healthSessionExist, healthSessionId);
  }
}
