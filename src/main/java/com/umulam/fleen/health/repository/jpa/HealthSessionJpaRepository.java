package com.umulam.fleen.health.repository.jpa;

import com.umulam.fleen.health.constant.base.ProfileType;
import com.umulam.fleen.health.model.domain.Country;
import com.umulam.fleen.health.model.domain.HealthSession;
import com.umulam.fleen.health.model.domain.Member;
import com.umulam.fleen.health.model.domain.Professional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface HealthSessionJpaRepository extends JpaRepository<HealthSession, Integer> {

  Optional<HealthSession> findByReference(String reference);

  Optional<HealthSession> findByPatientAndId(Member member, Integer healthSessionId);

  List<HealthSession> findByProfessionalAndDateAfter(Member member, LocalDate date);

  boolean existsById(Integer healthSessionId);

  Optional<HealthSession> findByProfessionalAndDateAndTime(Member member, LocalDate date, LocalTime time);

  @Query(value = "SELECT hs FROM HealthSession hs WHERE hs.patient.id = :memberId AND hs.patient.userType = :userType")
  Page<HealthSession> findSessionsByUser(@Param("memberId") Integer memberId, @Param("userType") ProfileType profileType,  Pageable pageable);

  @Query(value = "SELECT hs FROM HealthSession hs WHERE hs.patient.id = :memberId AND hs.patient.userType = :userType AND hs.createdOn BETWEEN :startDate AND :endDate")
  Page<HealthSession> findSessionsByUserAndDateBetween(@Param("memberId") Integer memberId, @Param("userType") ProfileType profileType,
                                                 @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, Pageable pageable);

  @Query(value = "SELECT hs FROM HealthSession hs WHERE hs.patient.id = :memberId AND hs.patient.userType = :userType")
  Page<HealthSession> findSessionsByProfessional(@Param("memberId") Integer memberId, @Param("userType") ProfileType profileType,  Pageable pageable);

  @Query(value = "SELECT hs FROM HealthSession hs WHERE hs.patient.id = :memberId AND hs.patient.userType = :userType AND hs.createdOn BETWEEN :startDate AND :endDate")
  Page<HealthSession> findSessionsByProfessionalAndDateBetween(@Param("memberId") Integer memberId, @Param("userType") ProfileType profileType,
                                                          @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, Pageable pageable);
}
