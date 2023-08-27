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

  private final RoleJpaRepository repository;
  private final static String DEFAULT_USER_ROLE = "USER";

  public RoleServiceImpl(RoleJpaRepository repository) {
    this.repository = repository;
  }

  @Override
  public Role getRole(Long id) {
    return repository.findById(id)
            .orElseThrow(() -> new RoleNotFoundException(id));
  }

  @Override
  public Role getRoleByCode(String code) {
    return repository.findByCode(Objects.isNull(code) ? DEFAULT_USER_ROLE : code)
            .orElseThrow(() -> new RoleNotFoundException(code));
  }

  @Override
  public List<Role> getRoles() {
    return repository.findAll();
  }

  @Override
  public List<Role> getRolesById(List<Long> ids) {
    return repository.findManyByIds(ids);
  }

  @Override
  public Role saveRole(RoleDto dto) {
    Role Role = dto.toRole();
    return repository.save(Role);
  }

  @Override
  public Role updateRole(Long id, RoleDto dto) {
    getRole(id);
    Role Role = dto.toRole();
    Role.setId(id);
    return repository.save(Role);
  }

  @Override
  public void deleteMany(DeleteIdsDto dto) {
    List<Role> roles = dto
            .getIds()
            .stream()
            .map(id -> Role.builder()
                    .id(id).build())
            .collect(Collectors.toList());
    repository.deleteAll(roles);
  }

  @Override
  public void deleteAllRole() {
    repository.deleteAll();
  }

  @Override
  public boolean isRoleExists(Long id) {
    return repository.findById(id).isPresent();
  }

}
