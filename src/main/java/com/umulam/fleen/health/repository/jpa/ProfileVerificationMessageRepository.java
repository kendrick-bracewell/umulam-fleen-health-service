package com.umulam.fleen.health.repository.jpa;

import com.umulam.fleen.health.constant.verification.ProfileVerificationMessageType;
import com.umulam.fleen.health.model.domain.ProfileVerificationMessage;
import com.umulam.fleen.health.model.response.profileverificationmessage.GetProfileVerificationMessages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProfileVerificationMessageRepository extends JpaRepository<ProfileVerificationMessage, Integer> {

  Optional<ProfileVerificationMessage> findByVerificationMessageType(ProfileVerificationMessageType messageType);

  boolean existsById(Integer id);

  @Query(value ="SELECT title from profile_verification_message", nativeQuery = true)
  List<GetProfileVerificationMessages> getTitles();
}
