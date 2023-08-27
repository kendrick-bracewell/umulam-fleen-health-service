package com.umulam.fleen.health.service;

import com.umulam.fleen.health.model.domain.Role;
import com.umulam.fleen.health.model.dto.RoleDto;
import com.umulam.fleen.health.model.response.other.DeleteIdsDto;

import java.util.List;

public interface RoleService {

  Role getRole(Long id);

  Role getRoleByCode(String code);

  List<Role> getRoles();

  List<Role> getRolesById(List<Long> ids);

  Role saveRole(RoleDto dto);

  Role updateRole(Long id, RoleDto dto);

  void deleteMany(DeleteIdsDto dto);

  void deleteAllRole();

  boolean isRoleExists(Long id);
}
