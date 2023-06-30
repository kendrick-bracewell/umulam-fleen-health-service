package com.umulam.fleen.health.startup.impl;

import com.umulam.fleen.health.model.domain.Role;
import com.umulam.fleen.health.repository.jpa.RoleJpaRepository;
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
public class RoleStartupService implements StartupService<Role> {

  private final RoleJpaRepository jpaRepository;

  @Override
  @Transactional
  @EventListener(ApplicationReadyEvent.class)
  public void seedRecords() {
    try {
      if (true) {
        return;
      }
      for (Role role : getRecords()) {
        Optional<Role> entry = jpaRepository.findByCode(role.getCode());
        if (entry.isPresent()) {
          continue;
        }

        Role newEntry = Role.builder()
                .title(role.getTitle())
                .code(role.getCode())
                .build();
        jpaRepository.save(newEntry);
      }
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
    }
  }

  @Override
  public String getFilePath() {
    return "json/role.json";
  }

  @Override
  public Class<Role> getClazz() {
    return Role.class;
  }
}
