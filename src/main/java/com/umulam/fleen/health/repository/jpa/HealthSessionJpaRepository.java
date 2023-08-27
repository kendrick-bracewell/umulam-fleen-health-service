package com.umulam.fleen.health.repository.jpa;

import com.umulam.fleen.health.constant.base.ProfileType;
import com.umulam.fleen.health.constant.session.HealthSessionStatus;
import com.umulam.fleen.health.model.domain.HealthSession;
import com.umulam.fleen.health.model.domain.Member;
import com.umulam.fleen.health.model.response.healthsession.GetUpdateHealthSessionNote;
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

public interface HealthSessionJpaRepository extends JpaRepository<HealthSession, Long> {

  Optional<HealthSession> findByReference(String reference);

  Optional<HealthSession> findByPatientAndId(Member member, Long healthSessionId);

  List<HealthSession> findByProfessionalAndDateAfter(Member member, LocalDate date);

  boolean existsById(Long healthSessionId);

  Optional<HealthSession> findByProfessionalAndDateAndTime(Member member, LocalDate date, LocalTime time);

  @Query(value = "SELECT hs FROM HealthSession hs WHERE hs.patient.id = :memberId AND hs.patient.userType = :userType")
  Page<HealthSession> findSessionsByUser(@Param("memberId") Long memberId, @Param("userType") ProfileType profileType,  Pageable pageable);

  @Query(value = "SELECT hs FROM HealthSession hs WHERE hs.patient.id = :memberId AND hs.patient.userType = :userType AND hs.createdOn BETWEEN :startDate AND :endDate")
  Page<HealthSession> findSessionsByUserAndDateBetween(@Param("memberId") Long memberId, @Param("userType") ProfileType profileType,
                                                       @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, Pageable pageable);

  @Query(value = "SELECT hs FROM HealthSession hs WHERE hs.professional.id = :memberId AND hs.professional.userType = :userType")
  Page<HealthSession> findSessionsByProfessional(@Param("memberId") Long memberId, @Param("userType") ProfileType profileType, Pageable pageable);

  @Query(value = "SELECT hs FROM HealthSession hs WHERE hs.patient.id = :memberId AND hs.patient.userType = :userType AND hs.createdOn BETWEEN :startDate AND :endDate")
  Page<HealthSession> findSessionsByProfessionalAndDateBetween(@Param("memberId") Long memberId, @Param("userType") ProfileType profileType,
                                                               @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, Pageable pageable);

  @Query(value = "SELECT hs FROM HealthSession hs WHERE hs.patient.id = :memberId AND hs.patient.userType = :userType AND hs.id = :healthSessionId")
  Optional<HealthSession> findSessionByUser(@Param("memberId") Long memberId, @Param("userType") ProfileType profileType, @Param("healthSessionId") Long healthSessionId);

  @Query(value = "SELECT hs FROM HealthSession hs WHERE hs.professional.id = :memberId AND hs.professional.userType = :userType AND hs.id = :healthSessionId")
  Optional<HealthSession> findSessionByProfessional(@Param("memberId") Long memberId, @Param("userType") ProfileType profileType, @Param("healthSessionId") Long healthSessionId);


  @Query(value = "SELECT DISTINCT hs.professional.id FROM HealthSession hs WHERE hs.patient.id = :memberId")
  List<Long> findAllProfessionalIdsOfUser(@Param("memberId") Long memberId);

  @Query(value ="SELECT note AS note, professional_id AS professionalId FROM health_session WHERE id = :healthSessionId", nativeQuery = true)
  Optional<GetUpdateHealthSessionNote> getUpdateHealthSessionNote(@Param("healthSessionId") Long healthSessionId);

  List<HealthSession> findByStatusOrStatusAndDate(HealthSessionStatus status, HealthSessionStatus status1, LocalDate date);

}
