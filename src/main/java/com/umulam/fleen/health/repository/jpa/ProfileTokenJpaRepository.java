package com.umulam.fleen.health.repository.jpa;

import com.umulam.fleen.health.model.domain.Member;
import com.umulam.fleen.health.model.domain.ProfileToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProfileTokenJpaRepository extends JpaRepository<ProfileToken, Long> {

  @Query("SELECT pt FROM ProfileToken pt WHERE pt.member.emailAddress = :emailAddress")
  Optional<ProfileToken> findProfileTokenByEmailAddress(String emailAddress);
}
