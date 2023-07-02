package com.umulam.fleen.health.util;

import com.umulam.fleen.health.constant.authentication.RoleType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

public class FleenAuthorities {

  public static final String ROLE_PREFIX = "ROLE_";

  public static List<GrantedAuthority> getUserPreVerifiedAuthorities() {
    return List.of(new SimpleGrantedAuthority(ROLE_PREFIX.concat(RoleType.PRE_VERIFIED_USER.name())));
  }

  public static List<GrantedAuthority> getProfessionalPreVerifiedAuthorities() {
    return List.of(new SimpleGrantedAuthority(ROLE_PREFIX.concat(RoleType.PRE_VERIFIED_PROFESSIONAL.name())));
  }

  public static List<GrantedAuthority> getBusinessPreVerifiedAuthorities() {
    return List.of(new SimpleGrantedAuthority(ROLE_PREFIX.concat(RoleType.PRE_VERIFIED_BUSINESS.name())));
  }

  public static List<GrantedAuthority> getRefreshTokenAuthorities() {
    return List.of(new SimpleGrantedAuthority(ROLE_PREFIX.concat(RoleType.REFRESH_TOKEN.name())));
  }

  public static List<GrantedAuthority> getPreAuthenticatedAuthorities() {
    return List.of(new SimpleGrantedAuthority(ROLE_PREFIX.concat(RoleType.PRE_AUTHENTICATED_USER.name())));
  }

  public static List<GrantedAuthority> buildAuthorities(List<String> roles) {
    return roles
            .stream()
            .map(role -> new SimpleGrantedAuthority(ROLE_PREFIX.concat(role)))
            .collect(Collectors.toList());
  }

}
