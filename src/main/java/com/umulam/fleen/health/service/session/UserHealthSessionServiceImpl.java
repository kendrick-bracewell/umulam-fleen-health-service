package com.umulam.fleen.health.service.session;

import com.umulam.fleen.health.constant.base.ProfileType;
import com.umulam.fleen.health.model.domain.HealthSession;
import com.umulam.fleen.health.model.request.search.base.SearchRequest;
import com.umulam.fleen.health.model.security.FleenUser;
import com.umulam.fleen.health.model.view.healthsession.HealthSessionView;
import com.umulam.fleen.health.model.view.healthsession.HealthSessionViewBasic;
import com.umulam.fleen.health.model.view.search.SearchResultView;
import com.umulam.fleen.health.repository.jpa.HealthSessionJpaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.umulam.fleen.health.model.mapper.HealthSessionMapper.toHealthSessionView;
import static com.umulam.fleen.health.model.mapper.HealthSessionMapper.toHealthSessionViewBasic;
import static com.umulam.fleen.health.util.FleenHealthUtil.areNotEmpty;
import static com.umulam.fleen.health.util.FleenHealthUtil.toSearchResult;

@Slf4j
@Service
public class UserHealthSessionServiceImpl implements UserHealthSessionService {

  private final HealthSessionJpaRepository healthSessionJpaRepository;

  public UserHealthSessionServiceImpl(HealthSessionJpaRepository healthSessionJpaRepository) {
    this.healthSessionJpaRepository = healthSessionJpaRepository;
  }

  @Override
  public SearchResultView viewSessions(FleenUser user, SearchRequest req) {
    Page<HealthSession> page;

    if (areNotEmpty(req.getStartDate(), req.getEndDate())) {
      page = healthSessionJpaRepository.findSessionsByUserAndDateBetween(user.getId(), ProfileType.USER, req.getStartDate().atStartOfDay(), req.getEndDate().atStartOfDay(), req.getPage());
    } else {
      page = healthSessionJpaRepository.findSessionsByUser(user.getId(), ProfileType.USER, req.getPage());
    }

    List<HealthSessionViewBasic> views = toHealthSessionViewBasic(page.getContent());
    return toSearchResult(views, page);
  }

  @Override
  public HealthSessionView viewSessionDetail(FleenUser user, Integer healthSessionId) {
    HealthSession healthSession = healthSessionJpaRepository.findSessionByUser(user.getId(), healthSessionId);
    return toHealthSessionView(healthSession);
  }
}
