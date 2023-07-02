package com.umulam.fleen.health.startup.impl;

import com.umulam.fleen.health.model.domain.MemberStatus;
import com.umulam.fleen.health.repository.jpa.MemberStatusJpaRepository;
import com.umulam.fleen.health.startup.StartupService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class MemberStatusStartupService implements StartupService<MemberStatus> {

  private final MemberStatusJpaRepository repository;

  @Override
  @Transactional
  @EventListener(ApplicationReadyEvent.class)
  public void seedRecords() {
    try {
      if (true) {
        return;
      }
      for (MemberStatus status : getRecords()) {
        Optional<MemberStatus> entry = repository.findByCode(status.getCode());
        if (entry.isPresent()) {
          continue;
        }

        MemberStatus newEntry = MemberStatus.builder()
                .title(status.getTitle())
                .code(status.getCode())
                .build();
        repository.save(newEntry);
      }
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
    }
  }

  @Override
  public String getFilePath() {
    return "json/member-status.json";
  }

  @Override
  public Class<MemberStatus> getClazz() {
    return MemberStatus.class;
  }
}
