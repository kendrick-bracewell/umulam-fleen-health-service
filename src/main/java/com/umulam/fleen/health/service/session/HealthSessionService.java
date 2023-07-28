package com.umulam.fleen.health.service.session;

import com.umulam.fleen.health.model.dto.healthsession.AddHealthSessionReviewDto;
import com.umulam.fleen.health.model.dto.healthsession.BookHealthSessionDto;
import com.umulam.fleen.health.model.dto.healthsession.ReScheduleHealthSessionDto;
import com.umulam.fleen.health.model.request.search.ProfessionalSearchRequest;
import com.umulam.fleen.health.model.response.healthsession.GetProfessionalBookSessionResponse;
import com.umulam.fleen.health.model.response.healthsession.PendingHealthSessionBookingResponse;
import com.umulam.fleen.health.model.response.healthsession.ProfessionalCheckAvailabilityResponse;
import com.umulam.fleen.health.model.security.FleenUser;
import com.umulam.fleen.health.model.view.professional.ProfessionalViewBasic;
import com.umulam.fleen.health.model.view.search.SearchResultView;
import org.springframework.transaction.annotation.Transactional;

public interface HealthSessionService {

  @Transactional(readOnly = true)
  SearchResultView viewProfessionals(ProfessionalSearchRequest searchRequest);

  @Transactional(readOnly = true)
  ProfessionalViewBasic viewProfessionalDetail(Integer professionalId);

  @Transactional
  PendingHealthSessionBookingResponse bookSession(BookHealthSessionDto dto, FleenUser user);

  @Transactional
  void cancelSession(FleenUser user, Integer sessionId);

  @Transactional(readOnly = true)
  ProfessionalCheckAvailabilityResponse viewProfessionalAvailability(FleenUser user, Integer professionalId);

  @Transactional(readOnly = true)
  GetProfessionalBookSessionResponse getProfessionalBookSession(FleenUser user, Integer professionalId);

  @Transactional
  void rescheduleSession(ReScheduleHealthSessionDto dto, FleenUser user, Integer healthSessionId);

  @Transactional
  void addSessionReview(AddHealthSessionReviewDto dto, FleenUser user, Integer healthSessionId);
}
