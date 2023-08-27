package com.umulam.fleen.health.repository.jpa;

import com.umulam.fleen.health.model.domain.MemberStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberStatusJpaRepository extends JpaRepository<MemberStatus, Long> {

  Optional<MemberStatus> findByCode(String code);
}
