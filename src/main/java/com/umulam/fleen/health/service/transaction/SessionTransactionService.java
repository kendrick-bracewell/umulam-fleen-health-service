package com.umulam.fleen.health.service.transaction;

import com.umulam.fleen.health.model.request.search.base.SearchRequest;
import com.umulam.fleen.health.model.security.FleenUser;
import com.umulam.fleen.health.model.view.search.SearchResultView;
import com.umulam.fleen.health.model.view.transaction.SessionTransactionView;

public interface SessionTransactionService {

  SearchResultView viewUserTransactions(FleenUser user, SearchRequest searchRequest);

  SessionTransactionView viewUserTransactionDetail(FleenUser user, Integer transactionId);
}
