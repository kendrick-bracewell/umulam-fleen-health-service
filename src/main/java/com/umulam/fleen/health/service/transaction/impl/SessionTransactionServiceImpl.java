package com.umulam.fleen.health.service.transaction.impl;

import com.umulam.fleen.health.exception.transaction.SessionTransactionNotFound;
import com.umulam.fleen.health.model.domain.transaction.SessionTransaction;
import com.umulam.fleen.health.model.mapper.SessionTransactionMapper;
import com.umulam.fleen.health.model.request.search.base.SearchRequest;
import com.umulam.fleen.health.model.security.FleenUser;
import com.umulam.fleen.health.model.view.search.SearchResultView;
import com.umulam.fleen.health.model.view.transaction.SessionTransactionView;
import com.umulam.fleen.health.model.view.transaction.SessionTransactionViewBasic;
import com.umulam.fleen.health.repository.jpa.transaction.SessionTransactionJpaRepository;
import com.umulam.fleen.health.service.transaction.SessionTransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.umulam.fleen.health.util.FleenHealthUtil.areNotEmpty;
import static com.umulam.fleen.health.util.FleenHealthUtil.toSearchResult;

@Slf4j
@Service
public class SessionTransactionServiceImpl implements SessionTransactionService {

  private final SessionTransactionJpaRepository sessionTransactionJpaRepository;

  public SessionTransactionServiceImpl(SessionTransactionJpaRepository sessionTransactionJpaRepository) {
    this.sessionTransactionJpaRepository = sessionTransactionJpaRepository;
  }

  @Override
  @Transactional(readOnly = true)
  public SearchResultView viewUserTransactions(FleenUser user, SearchRequest req) {
    Page<SessionTransaction> page;

    if (areNotEmpty(req.getStartDate(), req.getEndDate())) {
      page = sessionTransactionJpaRepository.findByDateBetween(user.getId(), req.getStartDate().atStartOfDay(), req.getEndDate().atStartOfDay(), req.getPage());
    } else {
      page = sessionTransactionJpaRepository.findAllByPayer(user.getId(), req.getPage());
    }

    List<SessionTransactionViewBasic> views = SessionTransactionMapper.toSessionTransactionViewBasic(page.getContent());
    return toSearchResult(views, page);
  }

  @Override
  @Transactional(readOnly = true)
  public SessionTransactionView viewUserTransactionDetail(FleenUser user, Integer transactionId) {
    Optional<SessionTransaction> sessionTransactionExist = sessionTransactionJpaRepository.findByUserAndId(transactionId, user.getId());
    if (sessionTransactionExist.isPresent()) {
      return SessionTransactionMapper.toSessionTransactionView(sessionTransactionExist.get());
    }
    throw new SessionTransactionNotFound(transactionId);
  }
}
