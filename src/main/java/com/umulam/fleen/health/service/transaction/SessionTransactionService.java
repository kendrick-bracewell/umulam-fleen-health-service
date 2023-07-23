package com.umulam.fleen.health.service.transaction;

import com.umulam.fleen.health.model.request.search.base.SearchRequest;
import com.umulam.fleen.health.model.security.FleenUser;

public interface SessionTransactionService {

  Object viewUserTransactions(FleenUser user, SearchRequest searchRequest);
}
