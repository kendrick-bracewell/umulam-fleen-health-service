package com.umulam.fleen.health.controller.admin;

import com.umulam.fleen.health.model.request.search.ProfessionalSearchRequest;
import com.umulam.fleen.health.model.view.ProfessionalView;
import com.umulam.fleen.health.model.view.SearchResultView;
import com.umulam.fleen.health.resolver.SearchParam;
import com.umulam.fleen.health.service.admin.AdminProfessionalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

import static com.umulam.fleen.health.constant.base.PagingConstant.*;
import static com.umulam.fleen.health.util.FleenHealthUtil.createPageable;

@Slf4j
@RestController
@RequestMapping(value = "admin/professional")
public class AdminProfessionalController {

  private final AdminProfessionalService service;

  public AdminProfessionalController(
          @Qualifier("adminProfessionalService") AdminProfessionalService service) {
    this.service = service;
  }

  @GetMapping(value = "/entries")
  public SearchResultView findProfessionals(@SearchParam ProfessionalSearchRequest request) {
    return service.findProfessionals(request);
  }

  @GetMapping(value = "/entries/pending-verification")
  public SearchResultView findProfessionalsPendingVerification(@SearchParam ProfessionalSearchRequest request) {
    return service.findProfessionalsVerificationStatus(request);
  }

  @GetMapping(value = "/detail/{professionalId}")
  public ProfessionalView findProfessionalDetail(@PathVariable(name = "professionalId") Integer id) {
    return service.findProfessionalById(id);
  }

  public void updateProfessionalDetail() {

  }

  public void updateProfessionalVerificationStatus() {

  }

  public void disableProfessional() {

  }

  public void updateProfessionalRole() {

  }
}
