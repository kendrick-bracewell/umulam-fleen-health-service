package com.umulam.fleen.health.repository.jpa.transaction;

import com.umulam.fleen.health.model.domain.transaction.WithdrawalTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.util.Optional;

public interface WithdrawalTransactionJpaRepository extends JpaRepository<WithdrawalTransaction, Long> {

  @Query(value = "SELECT wt FROM WithdrawalTransaction wt WHERE wt.reference = :reference")
  Optional<WithdrawalTransaction> findByReference(@Param("reference") String reference);

  @Query(value = "SELECT wt FROM WithdrawalTransaction wt WHERE wt.id = :transactionId AND wt.recipient.id = :memberId")
  Optional<WithdrawalTransaction> findByUserAndId(@PathVariable("transactionId") Long transactionId, @Param("memberId") Long memberId);

  @Query(value = "SELECT wt FROM WithdrawalTransaction wt WHERE wt.recipient.id = :memberId")
  Page<WithdrawalTransaction> findAllByPayer(@Param("memberId") Long memberId, Pageable pageable);

  @Query(value = "SELECT wt FROM WithdrawalTransaction wt WHERE wt.recipient.id = :memberId AND wt.createdOn BETWEEN :startDate AND :endDate")
  Page<WithdrawalTransaction> findByDateBetween(@Param("memberId") Long memberId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, Pageable pageable);
}
