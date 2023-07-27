package com.umulam.fleen.health.service.admin;

import com.umulam.fleen.health.model.request.search.SessionTransactionSearchRequest;
import com.umulam.fleen.health.model.view.search.SearchResultView;
import com.umulam.fleen.health.model.view.transaction.SessionTransactionView;

public interface AdminTransactionService {

  SearchResultView viewSessionTransactions(SessionTransactionSearchRequest searchRequest);

  SessionTransactionView viewSessionTransaction(Integer sessionTransactionId);
}
