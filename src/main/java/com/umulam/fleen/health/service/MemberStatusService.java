package com.umulam.fleen.health.service;

import com.umulam.fleen.health.model.domain.MemberStatus;
import com.umulam.fleen.health.model.dto.memberstatus.MemberStatusDto;
import com.umulam.fleen.health.model.dto.memberstatus.UpdateMemberStatusDto;
import com.umulam.fleen.health.model.response.other.DeleteIdsDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MemberStatusService {

  MemberStatus getMemberStatus(Long id);

  List<MemberStatus> getMemberStatuses();

  @Transactional(readOnly = true)
  MemberStatus getMemberStatusByCode(String code);

  @Transactional(readOnly = true)
  MemberStatus getReference(Long id);

  MemberStatus saveMemberStatus(MemberStatusDto dto);

  @Transactional
  MemberStatus updateMemberStatus(Long id, UpdateMemberStatusDto dto);

  void deleteMany(DeleteIdsDto ids);

  void deleteAllMemberStatus();

  boolean isMemberStatusExists(Long id);

  boolean isMemberStatusExistsByCode(String code);
}
