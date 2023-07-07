package com.umulam.fleen.health.service.impl;

import com.umulam.fleen.health.model.domain.ProfileToken;
import com.umulam.fleen.health.repository.jpa.ProfileTokenJpaRepository;
import com.umulam.fleen.health.service.ProfileTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class ProfileTokenServiceImpl implements ProfileTokenService {

  private final ProfileTokenJpaRepository repository;

  public ProfileTokenServiceImpl(ProfileTokenJpaRepository repository) {
    this.repository = repository;
  }

  @Override
  public ProfileToken save(ProfileToken token) {
    return repository.save(token);
  }

  @Override
  public Optional<ProfileToken> findByEmailAddress(String emailAddress) {
    return repository.findProfileTokenByEmailAddress(emailAddress);
  }
}
