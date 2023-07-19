package com.umulam.fleen.health.service;

import com.umulam.fleen.health.model.dto.healthsession.BookHealthSessionDto;
import com.umulam.fleen.health.model.request.search.ProfessionalSearchRequest;
import com.umulam.fleen.health.model.security.FleenUser;
import com.umulam.fleen.health.model.view.search.ProfessionalViewBasic;
import com.umulam.fleen.health.model.view.search.SearchResultView;

public interface HealthSessionService {

  SearchResultView viewProfessionals(ProfessionalSearchRequest searchRequest);

  ProfessionalViewBasic viewProfessionalDetail(Integer professionalId);

  void bookSession(BookHealthSessionDto dto, FleenUser user);

  void validateAndCompleteTransaction(String body);
}
