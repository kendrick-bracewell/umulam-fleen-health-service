package com.umulam.fleen.health.repository.jpa;

import com.umulam.fleen.health.model.domain.Country;
import com.umulam.fleen.health.model.domain.Professional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface CountryJpaRepository extends JpaRepository<Country, Long> {

  Optional<Country> findByCode(String code);

  @Query(value = "SELECT c FROM Country c WHERE c.createdOn BETWEEN :startDate AND :endDate")
  Page<Country> findByDateBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, Pageable pageable);
}
