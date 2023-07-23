package com.umulam.fleen.health.repository.jpa.transaction;

import com.umulam.fleen.health.model.domain.Country;
import com.umulam.fleen.health.model.domain.transaction.SessionTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface SessionTransactionJpaRepository extends JpaRepository<SessionTransaction, Integer> {

  @Query(value = "SELECT st FROM SessionTransaction st WHERE st.reference = :reference")
  Optional<SessionTransaction> findByReference(@Param("reference") String reference);

  @Query(value = "SELECT st FROM SessionTransaction st WHERE st.payer.id = :memberId")
  Page<SessionTransaction> findAllByPayer(@Param("memberId") Integer memberId, Pageable pageable);

  @Query(value = "SELECT st FROM SessionTransaction st WHERE st.payer.id = :memberId AND st.createdOn BETWEEN :startDate AND :endDate")
  Page<SessionTransaction> findByDateBetween(@Param("memberId") Integer memberId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, Pageable pageable);
}
