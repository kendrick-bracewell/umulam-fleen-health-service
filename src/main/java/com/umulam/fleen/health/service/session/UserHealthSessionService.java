package com.umulam.fleen.health.service.session;

import com.umulam.fleen.health.model.request.search.base.SearchRequest;
import com.umulam.fleen.health.model.security.FleenUser;

public interface UserHealthSessionService {

  Object viewSessions(FleenUser user, SearchRequest searchRequest);
}
