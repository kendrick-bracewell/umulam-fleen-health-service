package com.umulam.fleen.health.repository.jpa.admin.statistic;

import com.umulam.fleen.health.constant.base.ProfileType;
import com.umulam.fleen.health.constant.member.MemberGender;
import com.umulam.fleen.health.constant.verification.ProfileVerificationStatus;
import com.umulam.fleen.health.model.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AdminMemberStatisticJpaRepository extends JpaRepository<Member, Long> {

  @Query("SELECT COUNT(m) FROM Member m")
  long countTotalNumberOfMembers();

  @Query("SELECT COUNT(m) FROM Member m WHERE m.userType = :userType")
  long countTotalNumberOfMembers(@Param("userType") ProfileType profileType);

  @Query("SELECT COUNT(m) FROM Member m WHERE m.gender = :gender")
  long countTotalNumberOfMembersByGender(@Param("gender") MemberGender gender);

  @Query("SELECT COUNT(m) FROM Member m WHERE m.verificationStatus = :verificationStatus")
  long countTotalNumberOfMembersByVerificationStatus(@Param("verificationStatus") ProfileVerificationStatus verificationStatus);

  @Query("SELECT COUNT(m) FROM Member m WHERE m.emailAddressVerified = :verified")
  long countTotalNumberOfMembersByEmailAddressVerified(@Param("verified") boolean verified);

  @Query("SELECT COUNT(m) FROM Member m WHERE m.phoneNumberVerified = :verified")
  long countTotalNumberOfMembersByPhoneVerified(@Param("verified") boolean verified);

}
