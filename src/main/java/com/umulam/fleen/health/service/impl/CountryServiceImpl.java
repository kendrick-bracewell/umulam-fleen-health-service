package com.umulam.fleen.health.service.impl;

import com.umulam.fleen.health.exception.country.CountryCodeDuplicateException;
import com.umulam.fleen.health.exception.country.CountryNotFoundException;
import com.umulam.fleen.health.model.domain.Country;
import com.umulam.fleen.health.model.dto.country.CountryDto;
import com.umulam.fleen.health.model.dto.country.UpdateCountryDto;
import com.umulam.fleen.health.model.mapper.CountryMapper;
import com.umulam.fleen.health.model.request.search.CountrySearchRequest;
import com.umulam.fleen.health.model.response.other.DeleteIdsDto;
import com.umulam.fleen.health.model.view.CountryView;
import com.umulam.fleen.health.model.view.search.SearchResultView;
import com.umulam.fleen.health.repository.jpa.CountryJpaRepository;
import com.umulam.fleen.health.service.CountryService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.umulam.fleen.health.constant.authentication.AuthenticationConstant.COUNTRY_CACHE_PREFIX;
import static com.umulam.fleen.health.util.FleenHealthUtil.areNotEmpty;
import static com.umulam.fleen.health.util.FleenHealthUtil.toSearchResult;

@Slf4j
@Service
public class CountryServiceImpl implements CountryService {

  private final CountryJpaRepository repository;
  private final ModelMapper modelMapper;
  private final CacheService cacheService;

  public CountryServiceImpl(CountryJpaRepository repository,
                            ModelMapper mapper,
                            CacheService cacheService) {
    this.repository = repository;
    this.modelMapper = mapper;
    this.cacheService = cacheService;
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
  public SearchResultView findCountries(CountrySearchRequest req) {
    Page<Country> page;

    if (areNotEmpty(req.getStartDate(), req.getEndDate())) {
      page = repository.findByDateBetween(req.getStartDate().atStartOfDay(), req.getEndDate().atStartOfDay(), req.getPage());
    } else {
      page = repository.findAll(req.getPage());
    }

    List<CountryView> views = CountryMapper.toCountryViews(page.getContent());
    return toSearchResult(views, page);
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

  private String getCountryCacheKey() {
    return COUNTRY_CACHE_PREFIX;
  }

  @EventListener(ApplicationReadyEvent.class)
  public void saveCountriesToCacheOnStartup() {
    List<CountryView> countries = getCountriesForCache();
    saveCountriesToCache(countries);
  }

  @Scheduled(cron = "0 0 */12 * * *")
  private void saveCountriesToCache() {
    saveCountriesToCache(null);
  }

  private void saveCountriesToCache(List<CountryView> views) {
    if (views == null || views.isEmpty()) {
      views = getCountriesForCache();
    }
    String key = getCountryCacheKey();
    cacheService.set(key, views);
  }

  private List<CountryView> getCountriesForCache() {
    return CountryMapper.toCountryViews(repository.findAll());
  }

  @Override
  public List<?> getCountriesFromCache() {
    String key = getCountryCacheKey();
    return cacheService.get(key, List.class);
  }

}
