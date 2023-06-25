package com.umulam.fleen.health.controller;

import com.umulam.fleen.health.model.domain.Country;
import com.umulam.fleen.health.model.dto.CountryDto;
import com.umulam.fleen.health.model.response.other.DeleteIdsDto;
import com.umulam.fleen.health.model.response.other.DeleteResponse;
import com.umulam.fleen.health.model.view.CountryView;
import com.umulam.fleen.health.service.CountryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.umulam.fleen.health.model.mapper.CountryMapper.toCountryViews;

@Slf4j
@RestController
@RequestMapping(value = "country")
public class CountryController {

  private final CountryService countryService;

  public CountryController(CountryService countryService) {
    this.countryService = countryService;
  }

  @GetMapping(value = "/entries")
  public List<CountryView> findAll() {
    return toCountryViews(countryService.getCountries());
  }

  @GetMapping(value = "/detail/{id}")
  public Country findOne(@PathVariable(name = "id") Integer id) {
    return countryService.getCountry(id);
  }

  @PostMapping(value = "/create/save")
  public Country save(@Valid @RequestBody CountryDto dto) {
    return countryService.saveCountry(dto);
  }

  @PutMapping(value ="/update/{id}")
  public Country updateCountry(@PathVariable(name = "id") Integer id,  @Valid @RequestBody CountryDto dto) {
    return countryService.updateCountry(id, dto);
  }

  @ResponseBody
  @PostMapping(value ="/delete-many")
  public DeleteResponse deleteMany(@Valid @RequestBody DeleteIdsDto ids) {
    countryService.deleteMany(ids);
    return new DeleteResponse();
  }

  @PostMapping(value ="/delete-all")
  public DeleteResponse deleteAll() {
    countryService.deleteAllCountry();
    return new DeleteResponse();
  }

}
