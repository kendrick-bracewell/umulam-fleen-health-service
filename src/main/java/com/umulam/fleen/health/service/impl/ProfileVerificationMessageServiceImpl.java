package com.umulam.fleen.health.service.impl;

import com.umulam.fleen.health.constant.verification.ProfileVerificationMessageType;
import com.umulam.fleen.health.model.domain.ProfileVerificationMessage;
import com.umulam.fleen.health.repository.jpa.ProfileVerificationMessageRepository;
import com.umulam.fleen.health.service.ProfileVerificationMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ProfileVerificationMessageServiceImpl implements ProfileVerificationMessageService {

  private final ProfileVerificationMessageRepository repository;

  public ProfileVerificationMessageServiceImpl(ProfileVerificationMessageRepository repository) {
    this.repository = repository;
  }

  @Override
  public ProfileVerificationMessage getProfileVerificationMessageByType(@NonNull ProfileVerificationMessageType messageType) {
    return repository
            .findByVerificationMessageType(messageType)
            .orElse(null);
  }
}
