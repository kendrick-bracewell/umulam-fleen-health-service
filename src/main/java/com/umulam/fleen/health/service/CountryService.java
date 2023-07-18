package com.umulam.fleen.health.service;

import com.umulam.fleen.health.model.domain.Country;
import com.umulam.fleen.health.model.dto.country.CountryDto;
import com.umulam.fleen.health.model.dto.country.UpdateCountryDto;
import com.umulam.fleen.health.model.request.search.CountrySearchRequest;
import com.umulam.fleen.health.model.response.other.DeleteIdsDto;
import com.umulam.fleen.health.model.view.search.SearchResultView;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CountryService {

  Country getCountry(Integer id);

  @Transactional(readOnly = true)
  Country getCountryByCode(String code);

  @Transactional(readOnly = true)
  Country getReference(Integer id);

  SearchResultView findCountries(CountrySearchRequest searchRequest);

  Country saveCountry(CountryDto dto);

  Country updateCountry(Integer id, UpdateCountryDto dto);

  void deleteMany(DeleteIdsDto dto);

  void deleteAllCountry();

  boolean isCountryExists(Integer id);

  boolean isCountryExistsByCode(String code);

  List getCountriesFromCache();
}
