package com.umulam.fleen.health.controller;

import com.umulam.fleen.health.model.response.FleenHealthResponse;
import com.umulam.fleen.health.model.response.authentication.CreateEncodedPasswordResponse;
import com.umulam.fleen.health.model.security.FleenUser;
import com.umulam.fleen.health.service.AuthenticationService;
import com.umulam.fleen.health.service.CountryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.umulam.fleen.health.constant.base.FleenHealthConstant.SIGN_OUT;

@Slf4j
@RestController
@RequestMapping(value = "")
public class FleenHealthController {

  private final AuthenticationService authenticationService;
  private final CountryService countryService;

  public FleenHealthController(AuthenticationService authenticationService,
                               CountryService countryService) {
    this.authenticationService = authenticationService;
    this.countryService = countryService;
  }

  @GetMapping(value = "/sign-out")
  public FleenHealthResponse signOut(@AuthenticationPrincipal FleenUser user) {
    authenticationService.signOut(user.getUsername());
    return new FleenHealthResponse(SIGN_OUT);
  }

  @GetMapping(value = "/get-encoded-password")
//  @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'SUPER_ADMINISTRATOR')")
  public CreateEncodedPasswordResponse createEncodedPassword(@RequestParam(name = "password") String password) {
    String encodedPassword = authenticationService.createPassword(password);
    return new CreateEncodedPasswordResponse(encodedPassword, password);
  }

  @GetMapping(value = "/get-countries")
  public Object getCountries() {
    return countryService.getCountriesFromCache();
  }

}
