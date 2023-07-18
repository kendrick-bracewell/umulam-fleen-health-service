package com.umulam.fleen.health.service.admin;

import com.umulam.fleen.health.model.dto.admin.CreateMemberDto;
import com.umulam.fleen.health.model.request.search.MemberSearchRequest;
import com.umulam.fleen.health.model.view.search.SearchResultView;
import com.umulam.fleen.health.service.MemberService;
import org.springframework.transaction.annotation.Transactional;

public interface AdminMemberService extends MemberService {
  @Transactional(readOnly = true)
  SearchResultView findMembers(MemberSearchRequest req);

  @Transactional(readOnly = true)
  SearchResultView findPreOnboardedMembers(MemberSearchRequest req);

  @Transactional
  void createMember(CreateMemberDto dto);

  @Transactional
  void resendOnboardingDetails(Integer memberId);
}
