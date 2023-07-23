package com.umulam.fleen.health.service.session;

import com.umulam.fleen.health.model.request.search.base.SearchRequest;
import com.umulam.fleen.health.model.security.FleenUser;
import com.umulam.fleen.health.model.view.healthsession.HealthSessionView;
import com.umulam.fleen.health.model.view.search.SearchResultView;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

public interface ProfessionalHealthSessionService {

  SearchResultView viewSessions(FleenUser user, SearchRequest searchRequest);

  HealthSessionView viewSessionDetail(FleenUser user, Integer healthSessionId);
}
