package com.umulam.fleen.health.service.impl;

import com.umulam.fleen.health.exception.CountryNotFoundException;
import com.umulam.fleen.health.model.domain.Country;
import com.umulam.fleen.health.model.dto.CountryDto;
import com.umulam.fleen.health.model.response.other.DeleteIdsDto;
import com.umulam.fleen.health.repository.jpa.CountryJpaRepository;
import com.umulam.fleen.health.service.CountryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CountryServiceImpl implements CountryService {

  private final CountryJpaRepository repository;

  public CountryServiceImpl(CountryJpaRepository repository) {
    this.repository = repository;
  }

  @Override
  public Country getCountry(Integer id) {
    return repository
            .findById(id)
            .orElseThrow(() -> new CountryNotFoundException(id));
  }

  @Override
  public List<Country> getCountries() {
    return repository.findAll();
  }

  @Override
  @Transactional
  public Country saveCountry(CountryDto dto) {
    Country country = dto.toCountry();
    return repository.save(country);
  }

  @Override
  @Transactional
  public Country updateCountry(Integer id, CountryDto dto) {
    getCountry(id);
    Country country = dto.toCountry();
    country.setId(id);

    return repository.save(country);
  }

  @Override
  @Transactional
  public void deleteMany(DeleteIdsDto dto) {
    List<Country> countries = dto
            .getIds()
            .stream()
            .map(id -> Country.builder()
                    .id(id).build())
            .collect(Collectors.toList());

    repository.deleteAll(countries);
  }

  @Override
  public void deleteAllCountry() {
    repository.deleteAll();
  }

  @Override
  public boolean isCountryExists(Integer id) {
    return repository
            .findById(id)
            .isPresent();
  }

}
