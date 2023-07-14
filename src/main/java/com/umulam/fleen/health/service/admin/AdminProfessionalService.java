package com.umulam.fleen.health.service.admin;

import com.umulam.fleen.health.model.request.search.ProfessionalSearchRequest;
import com.umulam.fleen.health.model.view.SearchResultView;

public interface AdminProfessionalService {

  SearchResultView findProfessionals(ProfessionalSearchRequest searchRequest);

  SearchResultView findProfessionalsVerificationStatus(ProfessionalSearchRequest searchRequest);

}
