package com.umulam.fleen.health.repository.jpa;

import com.umulam.fleen.health.constant.verification.ProfileVerificationStatus;
import com.umulam.fleen.health.model.domain.Member;
import com.umulam.fleen.health.model.domain.MemberStatus;
import com.umulam.fleen.health.model.domain.Role;
import com.umulam.fleen.health.model.response.member.GetMemberUpdateDetailsResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

public interface MemberJpaRepository extends JpaRepository<Member, Integer> {

  Optional<Member> findByEmailAddress(String emailAddress);

  Optional<Member> findByPhoneNumber(String phoneNumber);

  @Modifying
  @Transactional
  @Query("UPDATE Member m SET m.mfaEnabled = true WHERE m.id = :id")
  void reEnableTwoFa(@Param("id") Integer memberId);

  @Modifying
  @Transactional
  @Query("UPDATE Member m SET m.mfaEnabled = false WHERE m.id = :id")
  void disableTwoFa(@Param("id") Integer memberId);

  @Query("SELECT m.mfaSecret FROM Member m WHERE m.id = :id")
  String getTwoFaSecret(@Param("id") Integer memberId);

  @Modifying
  @Transactional
  @Query("UPDATE Member m SET m.password = :password WHERE m.id = :id")
  void updatePassword(@Param("id") Integer memberId, String password);

  @Query("SELECT m.verificationStatus FROM Member m WHERE m.id = :id")
  ProfileVerificationStatus getProfileVerificationStatus(@Param("id") Integer memberId);

  boolean existsByEmailAddress(String emailAddress);

  boolean existsByPhoneNumber(String phoneNumber);

  @Query(value =
          "SELECT first_name as firstName, last_name as lastName, email_address as emailAddress," +
          "phone_number as phoneNumber, gender, date_of_birth as dateOfBirth from member where id = :id", nativeQuery = true)
  GetMemberUpdateDetailsResponse findMemberDetailsById(@Param("id") Integer memberId);

  @Modifying
  @Transactional
  @Query(value = "UPDATE Member m SET m.memberStatus = :memberStatus WHERE m.id = :id")
  void updateMemberStatus(@Param("id") Integer memberId, @Param("memberStatus") MemberStatus memberStatus);

  @Transactional(readOnly = true)
  @Query(value = "SELECT m.roles FROM Member m where m.id = :id")
  Set<Role> getMemberRole(@Param("id") Integer memberId);

}