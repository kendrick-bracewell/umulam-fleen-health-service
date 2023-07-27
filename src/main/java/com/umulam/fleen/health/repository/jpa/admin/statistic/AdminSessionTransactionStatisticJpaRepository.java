package com.umulam.fleen.health.repository.jpa.admin.statistic;

import com.umulam.fleen.health.constant.session.TransactionStatus;
import com.umulam.fleen.health.model.domain.transaction.SessionTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AdminSessionTransactionStatisticJpaRepository extends JpaRepository<SessionTransaction, Integer> {

  @Query("SELECT COUNT(st) FROM SessionTransaction st")
  long countTotalNumberOfSessionTransactions();

  @Query("SELECT COUNT(st) FROM SessionTransaction st WHERE st.status = :status")
  long countTotalNumberOfPendingSessionTransactions(@Param("status") TransactionStatus status);

  @Query("SELECT COUNT(st) FROM SessionTransaction st WHERE st.status = :status")
  long countTotalNumberOfSuccessfulSessionTransactions(@Param("status") TransactionStatus status);

  @Query("SELECT COUNT(st) FROM SessionTransaction st WHERE st.status = :status")
  long countTotalNumberOfFailedSessionTransactions(@Param("status") TransactionStatus status);

  @Query("SELECT COUNT(st) FROM SessionTransaction st WHERE st.status = :status")
  long countTotalNumberOfCanceledSessionTransactions(@Param("status") TransactionStatus status);

  @Query("SELECT COUNT(st) FROM SessionTransaction st WHERE st.status = :status")
  long countTotalNumberOfRefundedSessionTransactions(@Param("status") TransactionStatus status);
}
