package com.umulam.fleen.health.service;

import org.springframework.security.crypto.password.PasswordEncoder;

public interface PasswordService {

  default String createEncodedPassword(String rawPassword) {
    return getPasswordEncoder().encode(rawPassword);
  }

  PasswordEncoder getPasswordEncoder();
}
