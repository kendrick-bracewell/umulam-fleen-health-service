package com.umulam.fleen.health.util;

import com.umulam.fleen.health.constant.authentication.RoleType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

public class FleenAuthorities {

  public static List<GrantedAuthority> getPreVerifiedAuthorities() {
    return List.of(new SimpleGrantedAuthority("ROLE_".concat(RoleType.PRE_VERIFIED_USER.name())));
  }

  public static List<GrantedAuthority> getRefreshTokenAuthorities() {
    return List.of(new SimpleGrantedAuthority("ROLE_".concat(RoleType.REFRESH_TOKEN.name())));
  }

  public static List<GrantedAuthority> getPreAuthenticatedAuthorities() {
    return List.of(new SimpleGrantedAuthority("ROLE_".concat(RoleType.PRE_AUTHENTICATED_USER.name())));
  }

  public static List<GrantedAuthority> buildAuthorities(List<String> roles) {
    return roles
            .stream()
            .map(role -> new SimpleGrantedAuthority("ROLE_".concat(role)))
            .collect(Collectors.toList());
  }
}
