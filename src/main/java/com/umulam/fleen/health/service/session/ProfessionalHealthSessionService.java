package com.umulam.fleen.health.service.session;

import com.umulam.fleen.health.model.dto.healthsession.AddNoteHealthSessionDto;
import com.umulam.fleen.health.model.request.search.base.SearchRequest;
import com.umulam.fleen.health.model.response.healthsession.GetUpdateHealthSessionNote;
import com.umulam.fleen.health.model.security.FleenUser;
import com.umulam.fleen.health.model.view.healthsession.HealthSessionReviewView;
import com.umulam.fleen.health.model.view.healthsession.HealthSessionView;
import com.umulam.fleen.health.model.view.search.SearchResultView;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProfessionalHealthSessionService {

  SearchResultView viewSessions(FleenUser user, SearchRequest searchRequest);

  HealthSessionView viewSessionDetail(FleenUser user, Long healthSessionId);

  @Transactional(readOnly = true)
  GetUpdateHealthSessionNote getUpdateSessionNote(FleenUser user, Long healthSessionId);

  @Transactional
  void addSessionNote(AddNoteHealthSessionDto dto, FleenUser user, Long healthSessionId);

  List<HealthSessionReviewView> viewReviews(FleenUser user);
}
