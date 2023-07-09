package com.umulam.fleen.health.repository.jpa;

import com.umulam.fleen.health.model.domain.Professional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProfessionalJpaRepository extends JpaRepository<Professional, Integer> {

  @Query("SELECT b FROM Professional b WHERE b.member.emailAddress = :emailAddress")
  Optional<Professional> findProfessionalByEmailAddress(String emailAddress);
}
