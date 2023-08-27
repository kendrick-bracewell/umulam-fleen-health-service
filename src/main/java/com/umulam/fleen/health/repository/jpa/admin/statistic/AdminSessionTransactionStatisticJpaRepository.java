package com.umulam.fleen.health.repository.jpa.admin.statistic;

import com.umulam.fleen.health.constant.session.TransactionStatus;
import com.umulam.fleen.health.model.domain.transaction.SessionTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AdminSessionTransactionStatisticJpaRepository extends JpaRepository<SessionTransaction, Long> {

  @Query("SELECT COUNT(st) FROM SessionTransaction st")
  long countTotalNumberOfSessionTransactions();

  @Query("SELECT COUNT(st) FROM SessionTransaction st WHERE st.status = :status")
  long countTotalNumberOfSessionTransactions(@Param("status") TransactionStatus status);
}
