package com.umulam.fleen.health.repository.jpa;

import com.umulam.fleen.health.model.domain.Member;
import com.umulam.fleen.health.model.domain.MemberBankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BankAccountJpaRepository extends JpaRepository<MemberBankAccount, Integer> {

  boolean existsByAccountNumber(String accountNumber);
  Optional<MemberBankAccount> findByAccountNumberAndMember(String accountNumber, Member member);
}