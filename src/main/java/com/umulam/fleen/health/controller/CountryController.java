package com.umulam.fleen.health.controller;

import com.umulam.fleen.health.model.dto.country.CountryDto;
import com.umulam.fleen.health.model.dto.country.UpdateCountryDto;
import com.umulam.fleen.health.model.request.search.CountrySearchRequest;
import com.umulam.fleen.health.model.response.other.DeleteIdsDto;
import com.umulam.fleen.health.model.response.other.DeleteResponse;
import com.umulam.fleen.health.model.view.CountryView;
import com.umulam.fleen.health.model.view.search.SearchResultView;
import com.umulam.fleen.health.resolver.SearchParam;
import com.umulam.fleen.health.service.CountryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.umulam.fleen.health.model.mapper.CountryMapper.toCountryView;

@Slf4j
@RestController
@RequestMapping(value = "country")
public class CountryController {

  private final CountryService countryService;

  public CountryController(CountryService countryService) {
    this.countryService = countryService;
  }

  @GetMapping(value = "/entries")
  public SearchResultView findCountries(@SearchParam CountrySearchRequest searchRequest) {
    return countryService.findCountries(searchRequest);
  }

  @GetMapping(value = "/detail/{id}")
  public CountryView findOne(@PathVariable(name = "id") Integer id) {
    return toCountryView(countryService.getCountry(id));
  }

  @PostMapping(value = "/save")
  public CountryView save(@Valid @RequestBody CountryDto dto) {
    return toCountryView(countryService.saveCountry(dto));
  }

  @PutMapping(value ="/update/{id}")
  public CountryView updateCountry(@PathVariable(name = "id") Integer id, @Valid @RequestBody UpdateCountryDto dto) {
    return toCountryView(countryService.updateCountry(id, dto));
  }

  @DeleteMapping(value ="/delete-many")
  public DeleteResponse deleteMany(@Valid @RequestBody DeleteIdsDto ids) {
    countryService.deleteMany(ids);
    return new DeleteResponse();
  }

  @DeleteMapping(value ="/delete-all")
  public DeleteResponse deleteAll() {
    countryService.deleteAllCountry();
    return new DeleteResponse();
  }
}
