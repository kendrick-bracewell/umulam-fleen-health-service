package com.umulam.fleen.health.repository.jpa;

import com.umulam.fleen.health.model.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleJpaRepository extends JpaRepository<Role, Integer> {

  Optional<Role> findByCode(String code);
}
