package com.umulam.fleen.health.service.admin;

import com.umulam.fleen.health.model.request.search.HealthSessionSearchRequest;
import com.umulam.fleen.health.model.request.search.base.SearchRequest;
import com.umulam.fleen.health.model.view.healthsession.HealthSessionView;
import com.umulam.fleen.health.model.view.search.SearchResultView;
import com.umulam.fleen.health.service.session.HealthSessionService;
import org.springframework.transaction.annotation.Transactional;

public interface AdminHealthSessionService extends HealthSessionService {

  SearchResultView viewSessions(HealthSessionSearchRequest searchRequest);

  HealthSessionView viewSession(Integer healthSessionId);

  SearchResultView viewSessionReviews(SearchRequest searchRequest);

  @Transactional
  void cancelSession(Integer sessionId);
}
