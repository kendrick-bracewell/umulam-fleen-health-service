package com.umulam.fleen.health.repository.jpa;

import com.umulam.fleen.health.model.domain.transaction.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TransactionJpaRepository extends JpaRepository<Transaction, Integer> {

  Optional<Transaction> findByReference(String reference);

}
