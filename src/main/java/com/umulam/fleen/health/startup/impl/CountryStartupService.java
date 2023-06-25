package com.umulam.fleen.health.startup.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.umulam.fleen.health.model.domain.Country;
import com.umulam.fleen.health.repository.jpa.CountryJpaRepository;
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
public class CountryStartupService implements StartupService {

  private final CountryJpaRepository jpaRepository;
  private final ObjectMapper objectMapper;
  private static final String FILE_PATH = "json/country.json";

  @Override
  @Transactional
  @EventListener(ApplicationReadyEvent.class)
  public void seedRecords() {
    try {
      for (Country country : getRecords()) {
        Optional<Country> countryExists = jpaRepository.findByCode(country.getCode());
        if (countryExists.isPresent()) {
          continue;
        }

        Country newCountry = Country.builder()
                .title(country.getTitle())
                .code(country.getCode())
                .build();
        jpaRepository.save(newCountry);
      }
    } catch (Exception ignored) { }
  }

  public List<Country> getRecords() throws JsonProcessingException {
    String value = readResourceFile(FILE_PATH);
    return objectMapper.readValue(value, new TypeReference<>() {});
  }

}
