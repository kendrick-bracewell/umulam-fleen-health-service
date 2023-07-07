package com.umulam.fleen.health.service;

import com.umulam.fleen.health.model.domain.ProfileToken;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface ProfileTokenService {

  @Transactional
  ProfileToken save(ProfileToken token);

  Optional<ProfileToken> findByEmailAddress(String emailAddress);
}
