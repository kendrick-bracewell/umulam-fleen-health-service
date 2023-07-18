package com.umulam.fleen.health.repository.jpa;

import com.umulam.fleen.health.model.domain.Member;
import com.umulam.fleen.health.model.domain.ProfessionalAvailability;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfessionalAvailabilityJpaRepository extends JpaRepository<ProfessionalAvailability, Integer> {

  void deleteAllByMember(Member member);
}
