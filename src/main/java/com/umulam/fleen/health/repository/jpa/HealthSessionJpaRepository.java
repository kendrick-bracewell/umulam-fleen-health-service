package com.umulam.fleen.health.repository.jpa;

import com.umulam.fleen.health.model.domain.HealthSession;
import com.umulam.fleen.health.model.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HealthSessionJpaRepository extends JpaRepository<HealthSession, Integer> {

  Optional<HealthSession> findByReference(String reference);

  Optional<HealthSession> findByPatient(Member member);
}
