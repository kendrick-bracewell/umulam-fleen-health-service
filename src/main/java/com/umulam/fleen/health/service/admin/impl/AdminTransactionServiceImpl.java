package com.umulam.fleen.health.service.admin.impl;

import com.umulam.fleen.health.exception.transaction.SessionTransactionNotFound;
import com.umulam.fleen.health.model.domain.transaction.SessionTransaction;
import com.umulam.fleen.health.model.request.search.SessionTransactionSearchRequest;
import com.umulam.fleen.health.model.view.search.SearchResultView;
import com.umulam.fleen.health.model.view.transaction.SessionTransactionView;
import com.umulam.fleen.health.repository.jpa.admin.AdminSessionTransactionJpaRepository;
import com.umulam.fleen.health.repository.jpa.transaction.SessionTransactionJpaRepository;
import com.umulam.fleen.health.service.admin.AdminTransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.umulam.fleen.health.model.mapper.SessionTransactionMapper.toSessionTransactionView;
import static com.umulam.fleen.health.model.mapper.SessionTransactionMapper.toSessionTransactionViews;
import static com.umulam.fleen.health.util.FleenHealthUtil.areNotEmpty;
import static com.umulam.fleen.health.util.FleenHealthUtil.toSearchResult;
import static java.util.Objects.nonNull;

@Slf4j
@Service
public class AdminTransactionServiceImpl implements AdminTransactionService {

  private final AdminSessionTransactionJpaRepository adminSessionTransactionJpaRepository;
  private final SessionTransactionJpaRepository sessionTransactionJpaRepository;

  public AdminTransactionServiceImpl(AdminSessionTransactionJpaRepository adminSessionTransactionJpaRepository,
                                     SessionTransactionJpaRepository sessionTransactionJpaRepository) {
    this.adminSessionTransactionJpaRepository = adminSessionTransactionJpaRepository;
    this.sessionTransactionJpaRepository = sessionTransactionJpaRepository;
  }


  @Override
  public SearchResultView viewSessionTransactions(SessionTransactionSearchRequest req) {
    Page<SessionTransaction> page;

    if (areNotEmpty(req.getStartDate(), req.getEndDate())) {
      page = adminSessionTransactionJpaRepository.findByDateBetween(req.getStartDate().atStartOfDay(), req.getEndDate().atStartOfDay(), req.getPage());
    } else if (areNotEmpty(req.getReference())) {
      page = adminSessionTransactionJpaRepository.findByReference(req.getReference(), req.getPage());
    } else if (nonNull(req.getTransactionStatus())) {
      page = adminSessionTransactionJpaRepository.findByStatus(req.getTransactionStatus(), req.getPage());
    } else if (nonNull(req.getSessionReference())) {
      page = adminSessionTransactionJpaRepository.findBySessionReference(req.getSessionReference(), req.getPage());
    } else if (nonNull(req.getTransactionSubType())) {
      page = adminSessionTransactionJpaRepository.findByTransactionSubType(req.getTransactionSubType(), req.getPage());
    } else {
      page = adminSessionTransactionJpaRepository.findAll(req.getPage());
    }

    List<SessionTransactionView> views = toSessionTransactionViews(page.getContent());
    return toSearchResult(views, page);
  }

  @Override
  public SessionTransactionView viewSessionTransaction(Integer sessionTransactionId) {
    Optional<SessionTransaction> existingSessionTransaction = sessionTransactionJpaRepository.findById(sessionTransactionId);
    if (existingSessionTransaction.isPresent()) {
      return toSessionTransactionView(existingSessionTransaction.get());
    }
    throw new SessionTransactionNotFound(sessionTransactionId);
  }
}
