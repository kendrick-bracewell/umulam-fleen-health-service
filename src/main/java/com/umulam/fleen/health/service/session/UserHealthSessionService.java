package com.umulam.fleen.health.service.session;

import com.umulam.fleen.health.model.request.search.base.SearchRequest;
import com.umulam.fleen.health.model.security.FleenUser;
import com.umulam.fleen.health.model.view.search.SearchResultView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public interface UserHealthSessionService {

  SearchResultView viewSessions(FleenUser user, SearchRequest searchRequest);
}
