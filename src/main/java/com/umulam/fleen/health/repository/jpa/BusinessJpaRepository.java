package com.umulam.fleen.health.repository.jpa;

import com.umulam.fleen.health.model.domain.Business;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BusinessJpaRepository extends JpaRepository<Business, Integer> {

  @Query("SELECT b FROM Business b WHERE b.member.emailAddress = :emailAddress")
  Optional<Business> findBusinessByEmailAddress(String emailAddress);
}
