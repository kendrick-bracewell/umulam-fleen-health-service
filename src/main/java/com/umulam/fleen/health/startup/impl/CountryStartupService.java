package com.umulam.fleen.health.startup.impl;

import com.umulam.fleen.health.model.domain.Country;
import com.umulam.fleen.health.repository.jpa.CountryJpaRepository;
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
public class CountryStartupService implements StartupService<Country> {

  private final CountryJpaRepository jpaRepository;

  @Override
  @Transactional
  @EventListener(ApplicationReadyEvent.class)
  public void seedRecords() {
    try {
      if (true) {
        return;
      }
      for (Country country : getRecords()) {
        Optional<Country> entry = jpaRepository.findByCode(country.getCode());
        if (entry.isPresent()) {
          continue;
        }

        Country newEntry = Country.builder()
                .title(country.getTitle())
                .code(country.getCode())
                .build();
        jpaRepository.save(newEntry);
      }
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
    }
  }

  @Override
  public String getFilePath() {
    return "json/country.json";
  }

  @Override
  public Class<Country> getClazz() {
    return Country.class;
  }
}
