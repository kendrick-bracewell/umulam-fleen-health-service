package com.umulam.fleen.health.controller.admin;

import com.umulam.fleen.health.model.request.search.ProfessionalSearchRequest;
import com.umulam.fleen.health.resolver.SearchParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

import static com.umulam.fleen.health.constant.base.PagingConstant.*;
import static com.umulam.fleen.health.util.FleenHealthUtil.createPageable;

@Slf4j
@RestController
@RequestMapping(value = "admin/professional")
public class AdminProfessionalController {

  @GetMapping(value = "/test-view-professionals")
  public Object testViewProfessionals(@SearchParam ProfessionalSearchRequest dto) {
    return dto;
  }

  public void viewProfessionalDetail() {

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
