package com.umulam.fleen.health.startup.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.umulam.fleen.health.model.domain.MemberStatus;
import com.umulam.fleen.health.repository.jpa.MemberStatusJpaRepository;
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
public class MemberStatusStartupService implements StartupService {

  private final MemberStatusJpaRepository jpaRepository;
  private final ObjectMapper objectMapper;
  private static final String FILE_PATH = "json/member-status.json";

  @Override
  @Transactional
  @EventListener(ApplicationReadyEvent.class)
  public void seedRecords() {
    try {
      for (MemberStatus memberStatus : getRecords()) {
        Optional<MemberStatus> countryExists = jpaRepository.findByCode(memberStatus.getCode());
        if (countryExists.isPresent()) {
          continue;
        }

        MemberStatus newMemberStatus = MemberStatus.builder()
                .title(memberStatus.getTitle())
                .code(memberStatus.getCode())
                .build();
        jpaRepository.save(newMemberStatus);
      }
    } catch (Exception ignored) { }
  }

  public List<MemberStatus> getRecords() throws JsonProcessingException {
    String value = readResourceFile(FILE_PATH);
    return objectMapper.readValue(value, new TypeReference<>() {});
  }
}
