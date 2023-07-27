package com.umulam.fleen.health.repository.jpa.admin;

import com.umulam.fleen.health.constant.session.HealthSessionStatus;
import com.umulam.fleen.health.constant.session.SessionLocation;
import com.umulam.fleen.health.model.domain.HealthSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public interface AdminHealthSessionJpaRepository extends JpaRepository<HealthSession, Integer> {

  @Query(value = "SELECT hs FROM HealthSession hs WHERE hs.reference = :reference")
  Page<HealthSession> findByReference(@Param("reference") String reference, Pageable pageable);

  @Query(value = "SELECT hs FROM HealthSession hs WHERE hs.createdOn BETWEEN :startDate AND :endDate")
  Page<HealthSession> findByDateBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, Pageable pageable);

  @Query(value = "SELECT hs FROM HealthSession hs WHERE hs.location = :location")
  Page<HealthSession> findByLocation(@Param("location") SessionLocation location, Pageable pageable);

  @Query(value = "SELECT hs FROM HealthSession hs WHERE hs.status = :status")
  Page<HealthSession> findByStatus(@Param("status") HealthSessionStatus status, Pageable pageable);

  @Query(value = "SELECT hs FROM HealthSession hs WHERE hs.date = :date")
  Page<HealthSession> findByDate(@Param("date") LocalDate date, Pageable pageable);

  @Query(value = "SELECT hs FROM HealthSession hs WHERE hs.time = :time")
  Page<HealthSession> findByTime(@Param("time") LocalTime time, Pageable pageable);

  @Query(value = "SELECT hs FROM HealthSession hs WHERE hs.timezone = :timezone")
  Page<HealthSession> findByTimezone(@Param("timezone") String timezone, Pageable pageable);
}
