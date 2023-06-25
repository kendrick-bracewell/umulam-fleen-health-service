package com.umulam.fleen.health.service;

import com.umulam.fleen.health.model.domain.MemberStatus;
import com.umulam.fleen.health.model.dto.MemberStatusDto;
import com.umulam.fleen.health.model.response.other.DeleteIdsDto;

import java.util.List;

public interface MemberStatusService {

  MemberStatus getMemberStatus(Integer id);

  List<MemberStatus> getMemberStatuses();

  MemberStatus saveMemberStatus(MemberStatusDto dto);

  MemberStatus updateMemberStatus(Integer id, MemberStatusDto dto);

  void deleteMany(DeleteIdsDto ids);

  void deleteAllMemberStatus();

  boolean isMemberStatusExists(Integer id);
}
