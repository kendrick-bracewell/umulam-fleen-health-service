package com.umulam.fleen.health.repository.jpa.transaction;

import com.umulam.fleen.health.model.domain.transaction.SessionTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SessionTransactionJpaRepository extends JpaRepository<SessionTransaction, Integer> {

  @Query(value = "SELECT st FROM SessionTransaction st WHERE st.reference = :reference")
  Optional<SessionTransaction> findByReference(@Param("reference") String reference);

}
