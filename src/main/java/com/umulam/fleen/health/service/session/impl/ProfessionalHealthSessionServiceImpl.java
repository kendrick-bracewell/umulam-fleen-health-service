package com.umulam.fleen.health.service.session.impl;

import com.umulam.fleen.health.constant.base.ProfileType;
import com.umulam.fleen.health.exception.healthsession.HealthSessionNotFoundException;
import com.umulam.fleen.health.model.domain.HealthSession;
import com.umulam.fleen.health.model.domain.HealthSessionReview;
import com.umulam.fleen.health.model.mapper.HealthSessionReviewMapper;
import com.umulam.fleen.health.model.request.search.base.SearchRequest;
import com.umulam.fleen.health.model.security.FleenUser;
import com.umulam.fleen.health.model.view.healthsession.HealthSessionReviewView;
import com.umulam.fleen.health.model.view.healthsession.HealthSessionView;
import com.umulam.fleen.health.model.view.healthsession.HealthSessionViewBasic;
import com.umulam.fleen.health.model.view.search.SearchResultView;
import com.umulam.fleen.health.repository.jpa.HealthSessionJpaRepository;
import com.umulam.fleen.health.repository.jpa.HealthSessionReviewJpaRepository;
import com.umulam.fleen.health.service.session.ProfessionalHealthSessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.umulam.fleen.health.model.mapper.HealthSessionMapper.toHealthSessionView;
import static com.umulam.fleen.health.model.mapper.HealthSessionMapper.toHealthSessionViewBasic;
import static com.umulam.fleen.health.util.FleenHealthUtil.areNotEmpty;
import static com.umulam.fleen.health.util.FleenHealthUtil.toSearchResult;

@Slf4j
@Service
public class ProfessionalHealthSessionServiceImpl implements ProfessionalHealthSessionService {

  private final HealthSessionJpaRepository healthSessionJpaRepository;
  private final HealthSessionReviewJpaRepository healthSessionReviewJpaRepository;

  public ProfessionalHealthSessionServiceImpl(HealthSessionJpaRepository healthSessionJpaRepository,
                                              HealthSessionReviewJpaRepository healthSessionReviewJpaRepository) {
    this.healthSessionJpaRepository = healthSessionJpaRepository;
    this.healthSessionReviewJpaRepository = healthSessionReviewJpaRepository;
  }

  @Override
  @Transactional(readOnly = true)
  public SearchResultView viewSessions(FleenUser user, SearchRequest req) {
    Page<HealthSession> page;

    if (areNotEmpty(req.getStartDate(), req.getEndDate())) {
      page = healthSessionJpaRepository.findSessionsByProfessionalAndDateBetween(user.getId(), ProfileType.PROFESSIONAL, req.getStartDate().atStartOfDay(), req.getEndDate().atStartOfDay(), req.getPage());
    } else {
      page = healthSessionJpaRepository.findSessionsByProfessional(user.getId(), ProfileType.PROFESSIONAL, req.getPage());
    }

    List<HealthSessionViewBasic> views = toHealthSessionViewBasic(page.getContent());
    return toSearchResult(views, page);
  }

  @Override
  @Transactional(readOnly = true)
  public HealthSessionView viewSessionDetail(FleenUser user, Integer healthSessionId) {
    Optional<HealthSession> healthSessionExist = healthSessionJpaRepository.findSessionByProfessional(user.getId(), ProfileType.PROFESSIONAL, healthSessionId);
    if (healthSessionExist.isPresent()) {
      return toHealthSessionView(healthSessionExist.get());
    }
    throw new HealthSessionNotFoundException(healthSessionId);
  }

  @Override
  public List<HealthSessionReviewView> viewReviews(FleenUser user) {
    List<HealthSessionReview> sessionReviews = healthSessionReviewJpaRepository.findProfessionalReviews(user.getId());
    return HealthSessionReviewMapper.toHealthSessionReviewProfessionalViews(sessionReviews);
  }
}
