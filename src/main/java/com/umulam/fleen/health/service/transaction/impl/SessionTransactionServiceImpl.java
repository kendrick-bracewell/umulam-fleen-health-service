package com.umulam.fleen.health.service.transaction.impl;

import com.umulam.fleen.health.model.domain.transaction.SessionTransaction;
import com.umulam.fleen.health.model.security.FleenUser;
import com.umulam.fleen.health.repository.jpa.transaction.SessionTransactionJpaRepository;
import com.umulam.fleen.health.service.transaction.SessionTransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class SessionTransactionServiceImpl implements SessionTransactionService {

  private final SessionTransactionJpaRepository sessionTransactionJpaRepository;

  public SessionTransactionServiceImpl(SessionTransactionJpaRepository sessionTransactionJpaRepository) {
    this.sessionTransactionJpaRepository = sessionTransactionJpaRepository;
  }

  @Override
  public Object viewUserTransactions(FleenUser user) {
    List<SessionTransaction> sessionTransactions = sessionTransactionJpaRepository.findAllByPayer(user.getId());
    return null;
  }
}
