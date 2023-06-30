package com.umulam.fleen.health.service.impl;

import com.umulam.fleen.health.exception.role.RoleNotFoundException;
import com.umulam.fleen.health.model.domain.Role;
import com.umulam.fleen.health.model.dto.RoleDto;
import com.umulam.fleen.health.model.response.other.DeleteIdsDto;
import com.umulam.fleen.health.repository.jpa.RoleJpaRepository;
import com.umulam.fleen.health.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RoleServiceImpl implements RoleService {

  private final RoleJpaRepository roleJpaRepository;
  private final static String DEFAULT_USER_ROLE = "USER";

  public RoleServiceImpl(RoleJpaRepository roleJpaRepository) {
    this.roleJpaRepository = roleJpaRepository;
  }

  @Override
  public Role getRole(Integer id) {
    return roleJpaRepository.findById(id)
            .orElseThrow(() -> new RoleNotFoundException(id));
  }

  @Override
  public Role getRoleByCode(String code) {
    return roleJpaRepository.findByCode(Objects.isNull(code) ? DEFAULT_USER_ROLE : code)
            .orElseThrow(() -> new RoleNotFoundException(code));
  }

  @Override
  public List<Role> getRoles() {
    return roleJpaRepository.findAll();
  }

  @Override
  public Role saveRole(RoleDto dto) {
    Role Role = dto.toRole();
    return roleJpaRepository.save(Role);
  }

  @Override
  public Role updateRole(Integer id, RoleDto dto) {
    getRole(id);
    Role Role = dto.toRole();
    Role.setId(id);
    return roleJpaRepository.save(Role);
  }

  @Override
  public void deleteMany(DeleteIdsDto dto) {
    List<Role> roles = dto
            .getIds()
            .stream()
            .map(id -> Role.builder()
                    .id(id).build())
            .collect(Collectors.toList());
    roleJpaRepository.deleteAll(roles);
  }

  @Override
  public void deleteAllRole() {
    roleJpaRepository.deleteAll();
  }

  @Override
  public boolean isRoleExists(Integer id) {
    return roleJpaRepository.findById(id).isPresent();
  }

}
