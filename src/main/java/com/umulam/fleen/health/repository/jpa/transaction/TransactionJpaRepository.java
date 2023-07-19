package com.umulam.fleen.health.repository.jpa.transaction;

import com.umulam.fleen.health.model.domain.transaction.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionJpaRepository extends JpaRepository<Transaction, Integer> {

}
