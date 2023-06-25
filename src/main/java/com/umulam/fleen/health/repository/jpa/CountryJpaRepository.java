package com.umulam.fleen.health.repository.jpa;

import com.umulam.fleen.health.model.domain.Country;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CountryJpaRepository extends JpaRepository<Country, Integer> {

  Optional<Country> findByCode(String code);
}
