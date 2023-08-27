package com.umulam.fleen.health.repository.jpa;

import com.umulam.fleen.health.model.domain.ProfileVerificationHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationHistoryJpaRepository extends JpaRepository<ProfileVerificationHistory, Long> {
}
