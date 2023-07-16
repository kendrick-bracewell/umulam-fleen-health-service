package com.umulam.fleen.health.startup.impl;

import com.umulam.fleen.health.model.domain.ProfileVerificationMessage;
import com.umulam.fleen.health.repository.jpa.ProfileVerificationMessageJpaRepository;
import com.umulam.fleen.health.startup.StartupService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor
public class ProfileVerificationMessageStartupService implements StartupService<ProfileVerificationMessage> {
  
  private final ProfileVerificationMessageJpaRepository repository;

  @Override
  @Transactional
  @EventListener(ApplicationReadyEvent.class)
  public void seedRecords() {
    try {
      if (true) {
        return;
      }
      for (ProfileVerificationMessage verificationMessage : getRecords()) {

        ProfileVerificationMessage entry = ProfileVerificationMessage.builder()
                .verificationMessageType(verificationMessage.getVerificationMessageType())
                .title(verificationMessage.getTitle())
                .message(verificationMessage.getMessage())
                .htmlMessage(verificationMessage.getHtmlMessage())
                .plainText(verificationMessage.getPlainText())
                .build();
        repository.save(entry);
      }
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
    }
  }

  @Override
  public String getFilePath() {
    return "json/profile-verification-message.json";
  }

  @Override
  public Class<ProfileVerificationMessage> getClazz() {
    return ProfileVerificationMessage.class;
  }
}
