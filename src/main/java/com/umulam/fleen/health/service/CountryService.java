package com.umulam.fleen.health.service;

import com.umulam.fleen.health.model.domain.Country;
import com.umulam.fleen.health.model.dto.country.CountryDto;
import com.umulam.fleen.health.model.dto.country.UpdateCountryDto;
import com.umulam.fleen.health.model.response.other.DeleteIdsDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CountryService {

  Country getCountry(Integer id);

  @Transactional(readOnly = true)
  Country getCountryByCode(String code);

  @Transactional(readOnly = true)
  Country getReference(Integer id);

  List<Country> getCountries();

  Country saveCountry(CountryDto dto);

  Country updateCountry(Integer id, UpdateCountryDto dto);

  void deleteMany(DeleteIdsDto ids);

  void deleteAllCountry();

  boolean isCountryExists(Integer id);

  boolean isCountryExistsByCode(String code);
}
