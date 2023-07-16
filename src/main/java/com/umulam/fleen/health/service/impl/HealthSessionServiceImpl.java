package com.umulam.fleen.health.service.impl;

import com.umulam.fleen.health.constant.professional.ProfessionalAvailabilityStatus;
import com.umulam.fleen.health.model.domain.Professional;
import com.umulam.fleen.health.model.mapper.ProfessionalMapper;
import com.umulam.fleen.health.model.request.search.ProfessionalSearchRequest;
import com.umulam.fleen.health.model.view.ProfessionalViewBasic;
import com.umulam.fleen.health.model.view.SearchResultView;
import com.umulam.fleen.health.repository.jpa.HealthSessionJpaRepository;
import com.umulam.fleen.health.service.HealthSessionService;
import com.umulam.fleen.health.service.ProfessionalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.umulam.fleen.health.util.FleenHealthUtil.areNotEmpty;
import static com.umulam.fleen.health.util.FleenHealthUtil.toSearchResult;
import static java.util.Objects.nonNull;

@Slf4j
@Service
public class HealthSessionServiceImpl implements HealthSessionService {
  
  private final HealthSessionJpaRepository sessionJpaRepository;
  private final ProfessionalService professionalService;

  public HealthSessionServiceImpl(
          HealthSessionJpaRepository sessionJpaRepository,
          ProfessionalService professionalService) {
    this.sessionJpaRepository = sessionJpaRepository;
    this.professionalService = professionalService;
  }

  @Override
  public SearchResultView viewProfessionals(ProfessionalSearchRequest req) {
    Page<Professional> page;
    ProfessionalAvailabilityStatus availability = ProfessionalAvailabilityStatus.AVAILABLE;

    if (areNotEmpty(req.getFirstName(), req.getLastName())) {
      page = sessionJpaRepository.findByFirstNameAndLastName(req.getFirstName(), req.getLastName(), availability, req.getPage());
    } else if (nonNull(req.getProfessionalType())) {
      page = sessionJpaRepository.findByProfessionalType(req.getProfessionalType(), availability,  req.getPage());
    } else if (nonNull(req.getQualificationType())) {
      page = sessionJpaRepository.findByQualification(req.getQualificationType(), availability, req.getPage());
    } else if (nonNull(req.getLanguageSpoken())) {
      page = sessionJpaRepository.findByLanguageSpoken(req.getLanguageSpoken(), availability, req.getPage());
    } else {
      page = sessionJpaRepository.findByAvailabilityStatus(availability, req.getPage());
    }

    List<ProfessionalViewBasic> views = ProfessionalMapper.toProfessionalViewsBasic(page.getContent());
    return toSearchResult(views, page);
  }

  @Override
  public ProfessionalViewBasic viewProfessionalDetail(Integer professionalId) {
    professionalService.findProfessionalBasicById(professionalId);
  }
}
