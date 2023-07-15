package com.umulam.fleen.health.repository.jpa;

import com.umulam.fleen.health.constant.verification.ProfileVerificationMessageType;
import com.umulam.fleen.health.model.domain.ProfileVerificationMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileVerificationMessageRepository extends JpaRepository<ProfileVerificationMessage, Integer> {

  Optional<ProfileVerificationMessage> findByVerificationMessageType(ProfileVerificationMessageType messageType);

  boolean existsById(Integer id);
}
