package com.umulam.fleen.health.configuration.security.provider;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

  private final UserDetailsService userDetailsService;
  private final PasswordEncoder passwordEncoder;

  public CustomAuthenticationProvider(UserDetailsService userDetailsService,
                                      PasswordEncoder passwordEncoder) {
    this.userDetailsService = userDetailsService;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String emailAddress = authentication.getName();
    String password = authentication.getCredentials().toString();
    try {
      UserDetails detail = userDetailsService.loadUserByUsername(emailAddress);
      if (passwordEncoder.matches(password, detail.getPassword())) {
        return new UsernamePasswordAuthenticationToken(detail, null, detail.getAuthorities());
      }
    } catch (UsernameNotFoundException ex) {
      log.error(ex.getMessage(), ex);
    }
    return null;
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return authentication.equals(UsernamePasswordAuthenticationToken.class);
  }
}
