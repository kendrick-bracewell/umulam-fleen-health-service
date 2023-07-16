package com.umulam.fleen.health.repository.jpa;

import com.umulam.fleen.health.constant.verification.ProfileVerificationMessageType;
import com.umulam.fleen.health.model.domain.ProfileVerificationMessage;
import com.umulam.fleen.health.model.response.profileverificationmessage.GetProfileVerificationMessageId;
import com.umulam.fleen.health.model.response.profileverificationmessage.GetProfileVerificationMessages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProfileVerificationMessageRepository extends JpaRepository<ProfileVerificationMessage, Integer> {

  @Query("SELECT m FROM ProfileVerificationMessage m WHERE m.type = :type LIMIT 1")
  Optional<ProfileVerificationMessage> findByVerificationMessageType(@Param("type") ProfileVerificationMessageType messageType);

  boolean existsById(Integer id);

  @Query(value ="SELECT title from profile_verification_message", nativeQuery = true)
  List<GetProfileVerificationMessages> getTitles();

  @Query(value ="SELECT id from profile_verification_message WHERE id = :id", nativeQuery = true)
  GetProfileVerificationMessageId getId(@Param("id") Integer id);
}
