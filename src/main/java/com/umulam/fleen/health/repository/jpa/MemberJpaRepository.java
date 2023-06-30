package com.umulam.fleen.health.repository.jpa;

import com.umulam.fleen.health.model.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface MemberJpaRepository extends JpaRepository<Member, Integer> {

  Optional<Member> findByEmailAddress(String emailAddress);

  Optional<Member> findByPhoneNumber(String phoneNumber);

  @Modifying
  @Transactional
  @Query("UPDATE Member m SET m.mfaEnabled = true, m.mfaSecret = :secret WHERE m.id = :id")
  void enableTwoFa(@Param("id") Integer memberId, String secret);

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

  boolean existsByEmailAddress(String emailAddress);

  boolean existsByPhoneNumber(String phoneNumber);

}