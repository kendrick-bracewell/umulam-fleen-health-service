package com.umulam.fleen.health.repository.jpa;

import com.umulam.fleen.health.model.domain.Earnings;
import com.umulam.fleen.health.model.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EarningsJpaRepository extends JpaRepository<Earnings, Integer> {

  Optional<Earnings> findByMember(Member member);
}
