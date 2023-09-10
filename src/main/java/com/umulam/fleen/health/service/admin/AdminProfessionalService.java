package com.umulam.fleen.health.service.admin;

import com.umulam.fleen.health.model.dto.professional.UpdateProfessionalDetailsDto;
import com.umulam.fleen.health.model.dto.verification.UpdateProfileVerificationStatusDto;
import com.umulam.fleen.health.model.request.search.ProfessionalSearchRequest;
import com.umulam.fleen.health.model.view.professional.ProfessionalView;
import com.umulam.fleen.health.model.view.search.SearchResultView;
import com.umulam.fleen.health.service.ProfessionalService;
import org.springframework.transaction.annotation.Transactional;

public interface AdminProfessionalService extends ProfessionalService {

  SearchResultView findProfessionals(ProfessionalSearchRequest searchRequest);

  SearchResultView findProfessionalsByVerificationStatus(ProfessionalSearchRequest searchRequest);

  @Transactional
  ProfessionalView updateProfessionalDetail(UpdateProfessionalDetailsDto dto, Long professionalId);

  @Transactional
  void updateProfessionalVerificationStatus(UpdateProfileVerificationStatusDto dto, Long professionalId);

}
