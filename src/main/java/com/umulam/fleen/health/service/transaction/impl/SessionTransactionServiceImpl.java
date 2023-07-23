package com.umulam.fleen.health.service.transaction.impl;

import com.umulam.fleen.health.model.domain.transaction.SessionTransaction;
import com.umulam.fleen.health.model.mapper.CountryMapper;
import com.umulam.fleen.health.model.mapper.SessionTransactionMapper;
import com.umulam.fleen.health.model.request.search.base.SearchRequest;
import com.umulam.fleen.health.model.response.profileverificationmessage.GetProfileVerificationMessages;
import com.umulam.fleen.health.model.security.FleenUser;
import com.umulam.fleen.health.model.view.country.CountryView;
import com.umulam.fleen.health.model.view.transaction.SessionTransactionViewBasic;
import com.umulam.fleen.health.repository.jpa.transaction.SessionTransactionJpaRepository;
import com.umulam.fleen.health.service.transaction.SessionTransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.umulam.fleen.health.util.FleenHealthUtil.areNotEmpty;
import static com.umulam.fleen.health.util.FleenHealthUtil.toSearchResult;
import static java.util.Objects.nonNull;

@Slf4j
@Service
public class SessionTransactionServiceImpl implements SessionTransactionService {

  private final SessionTransactionJpaRepository sessionTransactionJpaRepository;

  public SessionTransactionServiceImpl(SessionTransactionJpaRepository sessionTransactionJpaRepository) {
    this.sessionTransactionJpaRepository = sessionTransactionJpaRepository;
  }

  @Override
  public Object viewUserTransactions(FleenUser user, SearchRequest req) {
    Page<SessionTransaction> page;

    if (areNotEmpty(req.getStartDate(), req.getEndDate())) {
      page = sessionTransactionJpaRepository.findByDateBetween(user.getId(), req.getStartDate().atStartOfDay(), req.getEndDate().atStartOfDay(), req.getPage());
    } else {
      page = sessionTransactionJpaRepository.findAllByPayer(user.getId(), req.getPage());
    }

    List<SessionTransactionViewBasic> views = SessionTransactionMapper.toSessionTransactionViewBasic(page.getContent());
    return toSearchResult(views, page);
  }
}
