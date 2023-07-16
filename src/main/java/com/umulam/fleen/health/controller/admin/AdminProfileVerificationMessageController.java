package com.umulam.fleen.health.controller.admin;

import com.umulam.fleen.health.model.dto.country.CountryDto;
import com.umulam.fleen.health.model.dto.country.UpdateCountryDto;
import com.umulam.fleen.health.model.response.other.DeleteIdsDto;
import com.umulam.fleen.health.model.response.other.DeleteResponse;
import com.umulam.fleen.health.model.response.profileverificationmessage.GetProfileVerificationMessages;
import com.umulam.fleen.health.model.view.CountryView;
import com.umulam.fleen.health.service.ProfileVerificationMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.umulam.fleen.health.model.mapper.CountryMapper.toCountryView;

@Slf4j
@RestController
@RequestMapping(value = "admin/profile-verification-message")
public class AdminProfileVerificationMessageController {
  
  private final ProfileVerificationMessageService service;
  
  public AdminProfileVerificationMessageController(ProfileVerificationMessageService service) {
    this.service = service;
  }
  
  @GetMapping(value = "/titles")
  public List<GetProfileVerificationMessages> getProfileVerificationMessageTitles() {
    return service.getTitles();
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
}
