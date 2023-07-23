package com.umulam.fleen.health.service.session;

import com.umulam.fleen.health.model.request.search.base.SearchRequest;
import com.umulam.fleen.health.model.security.FleenUser;
import com.umulam.fleen.health.model.view.healthsession.HealthSessionReviewView;
import com.umulam.fleen.health.model.view.healthsession.HealthSessionView;
import com.umulam.fleen.health.model.view.professional.ProfessionalView;
import com.umulam.fleen.health.model.view.search.SearchResultView;

import java.util.List;

public interface PatientHealthSessionService {

  SearchResultView viewSessions(FleenUser user, SearchRequest searchRequest);

  HealthSessionView viewSessionDetail(FleenUser user, Integer healthSessionId);

  List<ProfessionalView> viewProfessionalsOfPatient(FleenUser user);

  ProfessionalView viewProfessionalDetail(Integer id);

  List<HealthSessionReviewView> viewReviews(FleenUser user);
}
