package com.umulam.fleen.health.service;

import com.umulam.fleen.health.model.domain.Country;
import com.umulam.fleen.health.model.dto.CountryDto;
import com.umulam.fleen.health.model.response.other.DeleteIdsDto;

import java.util.List;

public interface CountryService {

  Country getCountry(Integer id);

  List<Country> getCountries();

  Country saveCountry(CountryDto dto);

  Country updateCountry(Integer id, CountryDto dto);

  void deleteMany(DeleteIdsDto ids);

  void deleteAllCountry();

  boolean isCountryExists(Integer id);
}
