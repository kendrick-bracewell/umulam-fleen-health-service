package com.umulam.fleen.health.service.admin;

import com.umulam.fleen.health.model.request.MemberSearchRequest;
import com.umulam.fleen.health.model.view.SearchResultView;
import com.umulam.fleen.health.service.MemberService;
import org.springframework.transaction.annotation.Transactional;

public interface AdminMemberService extends MemberService {
  @Transactional(readOnly = true)
  SearchResultView findMembers(MemberSearchRequest req);
}
