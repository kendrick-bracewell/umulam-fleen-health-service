package com.umulam.fleen.health.repository.jpa.admin.statistic;

import com.umulam.fleen.health.constant.session.HealthSessionStatus;
import com.umulam.fleen.health.model.domain.HealthSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AdminHealthSessionStatisticJpaRepository extends JpaRepository<HealthSession, Integer> {

  @Query("SELECT COUNT(hs) FROM HealthSession hs")
  long countTotalNumberOfSessions();

  @Query("SELECT COUNT(hs) FROM HealthSession hs WHERE hs.status = :status")
  long countTotalNumberOfPendingSessions(@Param("status") HealthSessionStatus status);

  @Query("SELECT COUNT(hs) FROM HealthSession hs WHERE hs.status = :status")
  long countTotalNumberOfCompletedSessions(@Param("status") HealthSessionStatus status);

  @Query("SELECT COUNT(hs) FROM HealthSession hs WHERE hs.status = :status")
  long countTotalNumberOfScheduleSessions(@Param("status") HealthSessionStatus status);

  @Query("SELECT COUNT(hs) FROM HealthSession hs WHERE hs.status = :status")
  long countTotalNumberOfRescheduledSessions(@Param("status") HealthSessionStatus status);

}
