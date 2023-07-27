package com.umulam.fleen.health.service.admin;

import com.umulam.fleen.health.model.request.search.HealthSessionSearchRequest;
import com.umulam.fleen.health.model.request.search.base.SearchRequest;
import com.umulam.fleen.health.model.view.healthsession.HealthSessionView;
import com.umulam.fleen.health.model.view.search.SearchResultView;

public interface AdminHealthSessionService {

  SearchResultView viewSessions(HealthSessionSearchRequest searchRequest);

  HealthSessionView viewSession(Integer healthSessionId);

  SearchResultView viewSessionReviews(SearchRequest searchRequest);

  void viewTransactions();

  void viewTransaction(Integer transactionId);
}
