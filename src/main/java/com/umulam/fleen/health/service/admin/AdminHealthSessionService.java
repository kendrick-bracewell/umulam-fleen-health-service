package com.umulam.fleen.health.service.admin;

import com.umulam.fleen.health.model.request.search.base.SearchRequest;

public interface AdminHealthSessionService {

  void viewSessions(SearchRequest searchRequest);

  void viewSession(Integer healthSessionId);

  void viewSessionReviews();

  void viewTransactions();

  void viewTransaction(Integer transactionId);
}
