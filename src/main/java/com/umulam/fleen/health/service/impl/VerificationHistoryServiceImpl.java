package com.umulam.fleen.health.service.impl;

import com.umulam.fleen.health.model.domain.ProfileVerificationHistory;
import com.umulam.fleen.health.repository.jpa.VerificationHistoryJpaRepository;
import com.umulam.fleen.health.service.VerificationHistoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VerificationHistoryServiceImpl implements VerificationHistoryService {

  private final VerificationHistoryJpaRepository repository;

  public VerificationHistoryServiceImpl(VerificationHistoryJpaRepository repository) {
    this.repository = repository;
  }

  @Override
  @Transactional
  public ProfileVerificationHistory saveVerificationHistory(ProfileVerificationHistory history) {
    return repository.save(history);
  }
}
 
 
