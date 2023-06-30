package com.umulam.fleen.health.service.impl;

import com.umulam.fleen.health.exception.country.CountryCodeDuplicateException;
import com.umulam.fleen.health.exception.country.CountryNotFoundException;
import com.umulam.fleen.health.model.domain.Country;
import com.umulam.fleen.health.model.dto.country.CountryDto;
import com.umulam.fleen.health.model.dto.country.UpdateCountryDto;
import com.umulam.fleen.health.model.response.other.DeleteIdsDto;
import com.umulam.fleen.health.repository.jpa.CountryJpaRepository;
import com.umulam.fleen.health.service.CountryService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CountryServiceImpl implements CountryService {

  private final CountryJpaRepository repository;
  private final ModelMapper modelMapper;

  public CountryServiceImpl(CountryJpaRepository repository,
                            ModelMapper mapper) {
    this.repository = repository;
    this.modelMapper = mapper;
  }

  @Override
  @Transactional(readOnly = true)
  public Country getCountry(Integer id) {
    return repository
            .findById(id)
            .orElseThrow(() -> new CountryNotFoundException(id));
  }

  @Override
  @Transactional(readOnly = true)
  public Country getCountryByCode(String code) {
    return repository
            .findByCode(code)
            .orElseThrow(() -> new CountryNotFoundException(code));
  }

  @Override
  @Transactional(readOnly = true)
  public Country getReference(Integer id) {
    return Optional
            .of(repository.getReferenceById(id))
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
  public Country updateCountry(Integer id, UpdateCountryDto dto) {
    getCountry(id);
    Country country = getCountryByCode(dto.getCode());
    if (isCountryExistsByCode(dto.getCode()) && !(country.getId().intValue() == id.intValue())) {
      throw new CountryCodeDuplicateException(dto.getCode());
    }

    modelMapper.map(dto, country);
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

  @Override
  public boolean isCountryExistsByCode(String code) {
    return repository
            .findByCode(code)
            .isPresent();
  }

}
