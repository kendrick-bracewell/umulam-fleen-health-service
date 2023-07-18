package com.umulam.fleen.health.repository.jpa;

import com.umulam.fleen.health.model.domain.Member;
import com.umulam.fleen.health.model.domain.ProfessionalAvailability;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProfessionalAvailabilityJpaRepository extends JpaRepository<ProfessionalAvailability, Integer> {

  void deleteAllByMember(Member member);

  List<ProfessionalAvailability> findAllByMember(Member member);
}
