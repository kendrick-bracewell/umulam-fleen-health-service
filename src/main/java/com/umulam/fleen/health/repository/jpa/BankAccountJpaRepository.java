package com.umulam.fleen.health.repository.jpa;

import com.umulam.fleen.health.model.domain.Member;
import com.umulam.fleen.health.model.domain.MemberBankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BankAccountJpaRepository extends JpaRepository<MemberBankAccount, Long> {

  boolean existsByAccountNumber(String accountNumber);
  Optional<MemberBankAccount> findByAccountNumberAndMember(String accountNumber, Member member);

  Optional<MemberBankAccount> findByIdAndMember(Long id, Member member);

  List<MemberBankAccount> findAllByMember(Member member);

  boolean existsById(Long id);

}