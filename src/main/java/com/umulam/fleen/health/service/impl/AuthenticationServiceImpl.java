package com.umulam.fleen.health.service.impl;

import com.umulam.fleen.health.model.security.UserDetailsImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

  private final AuthenticationManager authenticationManager;
  private final UserDetailsService userDetailsService;

  public AuthenticationServiceImpl(AuthenticationManager authenticationManager,
                                   UserDetailsService userDetailsService) {
    this.authenticationManager = authenticationManager;
    this.userDetailsService = userDetailsService;
  }

  @Override
  public String getLoggedInUserEmailAddress() {
    Object userDetails = SecurityContextHolder.getContext().getAuthentication().getDetails();
    if (userDetails instanceof UserDetailsImpl) {
      return ((UserDetails) userDetails).getUsername();
    }
    return null;
  }

  @Override
  public void authenticateAfterSignUp(String emailAddress, String password) {
    UserDetails userDetails = userDetailsService.loadUserByUsername(emailAddress);
    UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(
                    userDetails,
                    password,
                    userDetails.getAuthorities());

    authenticationManager.authenticate(authenticationToken);
    if (authenticationToken.isAuthenticated()) {
      SecurityContextHolder.getContext().setAuthentication(authenticationToken);
      log.info(
        String.format("User %s has been logged in",
                emailAddress));
    }


  }
}
