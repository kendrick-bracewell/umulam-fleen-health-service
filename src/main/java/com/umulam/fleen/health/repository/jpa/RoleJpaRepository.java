package com.umulam.fleen.health.repository.jpa;

import com.umulam.fleen.health.model.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RoleJpaRepository extends JpaRepository<Role, Integer> {

  Optional<Role> findByCode(String code);

  @Query("SELECT r FROM Role r WHERE r.id IN (:ids)")
  List<Role> findManyByIds(@Param("ids") List<Integer> ids);
}
