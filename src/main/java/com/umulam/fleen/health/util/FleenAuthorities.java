package com.umulam.fleen.health.util;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

import static com.umulam.fleen.health.constant.authentication.RoleType.*;

public class FleenAuthorities {

  public static final String ROLE_PREFIX = "ROLE_";

  public static List<GrantedAuthority> getUserPreVerifiedAuthorities() {
    return List.of(new SimpleGrantedAuthority(ROLE_PREFIX.concat(PRE_VERIFIED_USER.name())));
  }

  public static List<GrantedAuthority> getProfessionalPreVerifiedAuthorities() {
    return List.of(new SimpleGrantedAuthority(ROLE_PREFIX.concat(PRE_VERIFIED_PROFESSIONAL.name())));
  }

  public static List<GrantedAuthority> getBusinessPreVerifiedAuthorities() {
    return List.of(new SimpleGrantedAuthority(ROLE_PREFIX.concat(PRE_VERIFIED_BUSINESS.name())));
  }

  public static List<GrantedAuthority> getRefreshTokenAuthorities() {
    return List.of(new SimpleGrantedAuthority(ROLE_PREFIX.concat(REFRESH_TOKEN_USER.name())));
  }

  public static List<GrantedAuthority> getPreAuthenticatedAuthorities() {
    return List.of(new SimpleGrantedAuthority(ROLE_PREFIX.concat(PRE_AUTHENTICATED_USER.name())));
  }

  public static List<GrantedAuthority> getResetPasswordAuthorities() {
    return List.of(new SimpleGrantedAuthority(ROLE_PREFIX.concat(RESET_PASSWORD.name())));
  }

  public static List<GrantedAuthority> buildAuthorities(List<String> roles) {
    return roles
            .stream()
            .map(role -> new SimpleGrantedAuthority(ROLE_PREFIX.concat(role)))
            .collect(Collectors.toList());
  }

}
