package com.umulam.fleen.health.repository.jpa;

import com.umulam.fleen.health.model.domain.HealthSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HealthSessionJpaRepository extends JpaRepository<HealthSession, Integer> {
}
