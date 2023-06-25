package com.umulam.fleen.health.startup.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.umulam.fleen.health.model.domain.Role;
import com.umulam.fleen.health.repository.jpa.RoleJpaRepository;
import com.umulam.fleen.health.startup.StartupService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.umulam.fleen.health.util.FleenHealthUtil.readResourceFile;

@Slf4j
@Service
@AllArgsConstructor
public class RoleStartupService implements StartupService {

  private final RoleJpaRepository jpaRepository;
  private final ObjectMapper objectMapper;
  private static final String FILE_PATH = "json/role.json";

  @Override
  @Transactional
  @EventListener(ApplicationReadyEvent.class)
  public void seedRecords() {
    try {
      for (Role role : getRecords()) {
        Optional<Role> countryExists = jpaRepository.findByCode(role.getCode());
        if (countryExists.isPresent()) {
          continue;
        }

        Role newRole = Role.builder()
                .title(role.getTitle())
                .code(role.getCode())
                .build();
        jpaRepository.save(newRole);
      }
    } catch (Exception ignored) { }
  }

  public List<Role> getRecords() throws JsonProcessingException {
    String value = readResourceFile(FILE_PATH);
    return objectMapper.readValue(value, new TypeReference<>() {});
  }
}
