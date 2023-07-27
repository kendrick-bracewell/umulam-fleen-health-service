package com.umulam.fleen.health.repository.jpa.admin;

import com.umulam.fleen.health.constant.session.TransactionStatus;
import com.umulam.fleen.health.constant.session.TransactionSubType;
import com.umulam.fleen.health.model.domain.HealthSession;
import com.umulam.fleen.health.model.domain.transaction.SessionTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.time.LocalTime;

public interface AdminSessionTransactionJpaRepository extends JpaRepository<SessionTransaction, Integer> {

  @Query(value = "SELECT st FROM SessionTransaction st WHERE st.reference = :reference")
  Page<SessionTransaction> findByReference(@Param("reference") String reference, Pageable pageable);

  @Query(value = "SELECT st FROM SessionTransaction st WHERE st.sessionReference = :reference")
  Page<SessionTransaction> findBySessionReference(@Param("reference") String reference, Pageable pageable);

  @Query(value = "SELECT st FROM SessionTransaction st WHERE st.createdOn BETWEEN :startDate AND :endDate")
  Page<SessionTransaction> findByDateBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, Pageable pageable);

  @Query(value = "SELECT st FROM SessionTransaction st WHERE st.status = :transactionStatus")
  Page<SessionTransaction> findByStatus(@Param("transactionStatus") TransactionStatus status, Pageable pageable);

  @Query(value = "SELECT st FROM SessionTransaction st WHERE st.subType = :transactionSubType")
  Page<SessionTransaction> findByTransactionSubType(@Param("transactionSubType") TransactionSubType subType, Pageable pageable);

  @Query(value = "SELECT hs FROM HealthSession hs WHERE hs.time = :time")
  Page<HealthSession> findByTime(@Param("time") LocalTime time, Pageable pageable);

  @Query(value = "SELECT hs FROM HealthSession hs WHERE hs.timezone = :timezone")
  Page<HealthSession> findByTimezone(@Param("timezone") String timezone, Pageable pageable);
}
