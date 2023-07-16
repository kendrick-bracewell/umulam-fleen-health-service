package com.umulam.fleen.health.repository.jpa;

import com.umulam.fleen.health.constant.verification.ProfileVerificationMessageType;
import com.umulam.fleen.health.model.domain.ProfileVerificationMessage;
import com.umulam.fleen.health.model.response.profileverificationmessage.GetProfileVerificationMessageId;
import com.umulam.fleen.health.model.response.profileverificationmessage.GetProfileVerificationMessages;
import com.umulam.fleen.health.model.response.profileverificationmessage.GetProfileVerificationMessagesBasic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ProfileVerificationMessageJpaRepository extends JpaRepository<ProfileVerificationMessage, Integer> {

  @Query(value = "SELECT m FROM ProfileVerificationMessage m WHERE m.verification_message_type = :type LIMIT 1", nativeQuery = true)
  Optional<ProfileVerificationMessage> findByVerificationMessageType(@Param("type") ProfileVerificationMessageType messageType);

  boolean existsById(Integer id);

  @Query(value ="SELECT id, title from profile_verification_message", nativeQuery = true)
  List<GetProfileVerificationMessagesBasic> getBasicDetails();

  @Query(value ="SELECT id from profile_verification_message WHERE id = :id", nativeQuery = true)
  GetProfileVerificationMessageId getId(@Param("id") Integer id);

  @Query(
          value = "SELECT m.id, m.title, m.verification_message_type as verificationMessageType, m.created_on as createdOn, m.updated_on as updatedOn" +
                  " FROM profile_verification_message m WHERE m.createdOn BETWEEN :startDate AND :endDate",
          nativeQuery = true)
  Page<GetProfileVerificationMessages> findByDateBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, Pageable pageable);

  @Query(
          value = "SELECT m.id, m.title, m.verification_message_type as verificationMessageType, m.created_on as createdOn, m.updated_on as updatedOn" +
                  " FROM profile_verification_message m",
          nativeQuery = true)
  Page<GetProfileVerificationMessages> findByVerificationMessageType(@Param("messageType") ProfileVerificationMessageType verificationMessageType, Pageable pageable);

  @Query(
          value = "SELECT m.id, m.title, m.verification_message_type as verificationMessageType, m.created_on as createdOn, m.updated_on as updatedOn" +
                  " FROM profile_verification_message m",
          nativeQuery = true)
  Page<GetProfileVerificationMessages> findAllBasic(Pageable pageable);
}
