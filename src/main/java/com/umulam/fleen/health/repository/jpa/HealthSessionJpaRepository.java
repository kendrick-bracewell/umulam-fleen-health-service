package com.umulam.fleen.health.repository.jpa;

import com.umulam.fleen.health.model.domain.HealthSession;
import com.umulam.fleen.health.model.domain.Member;
import com.umulam.fleen.health.model.domain.Professional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface HealthSessionJpaRepository extends JpaRepository<HealthSession, Integer> {

  Optional<HealthSession> findByReference(String reference);

  Optional<HealthSession> findByPatient(Member member);

  List<HealthSession> findByProfessionalAfter(Member member, LocalDate date);
}
