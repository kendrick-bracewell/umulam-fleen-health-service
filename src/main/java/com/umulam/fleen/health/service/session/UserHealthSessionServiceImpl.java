package com.umulam.fleen.health.service.session;

import com.umulam.fleen.health.constant.base.ProfileType;
import com.umulam.fleen.health.model.domain.Country;
import com.umulam.fleen.health.model.domain.HealthSession;
import com.umulam.fleen.health.model.domain.Member;
import com.umulam.fleen.health.model.request.search.base.SearchRequest;
import com.umulam.fleen.health.model.security.FleenUser;
import com.umulam.fleen.health.repository.jpa.HealthSessionJpaRepository;
import com.umulam.fleen.health.service.MemberService;
import org.springframework.data.domain.Page;

import java.util.List;

import static com.umulam.fleen.health.util.FleenHealthUtil.areNotEmpty;

public class UserHealthSessionServiceImpl implements UserHealthSessionService {

  private final HealthSessionJpaRepository healthSessionJpaRepository;
  private final MemberService memberService;

  public UserHealthSessionServiceImpl(HealthSessionJpaRepository healthSessionJpaRepository,
                                      MemberService memberService) {
    this.healthSessionJpaRepository = healthSessionJpaRepository;
    this.memberService = memberService;
  }

  @Override
  public Object viewSessions(FleenUser user, SearchRequest req) {
    Member member = memberService.getMemberById(user.getId());
    Page<HealthSession> page;

    if (areNotEmpty(req.getStartDate(), req.getEndDate())) {
      page = healthSessionJpaRepository.findSessionsByUserAndDateBetween(user.getId(), ProfileType.USER, req.getStartDate().atStartOfDay(), req.getEndDate().atStartOfDay(), req.getPage());
    } else {
      page = healthSessionJpaRepository.findSessionsByUser(user.getId(), ProfileType.USER, req.getPage());
    }


    return null;
  }
}
