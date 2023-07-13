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

  public void viewProfessionals(@RequestParam(name = "page_no", defaultValue = DEFAULT_PAGE_NUMBER, required = false) Integer pageNo,
                                @RequestParam(name = "page_size", defaultValue = DEFAULT_PAGE_SIZE, required = false) Integer pageSize,
                                @RequestParam(name = "sort_dir", defaultValue = DEFAULT_SORT_DIRECTION, required = false) String sortDir,
                                @RequestParam(name = "sort_by", defaultValue = DEFAULT_SORT_BY, required = false) String sortBy,
                                @RequestParam(value = "from", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fromDate,
                                @RequestParam(value = "to", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date toDate,
                                @PageableDefault(page = 0, size = 20)
                                @SortDefault.SortDefaults({
                                        @SortDefault(sort = "id", direction = Sort.Direction.ASC)
                                })
                                Pageable pageable) {
    createPageable(pageNo, pageSize, sortBy, sortDir);
    PageRequest.of(1, 1);
    Sort.by("professionalType");
    PageRequest.of(1, 2, Sort.by("professionalType").descending());
    Sort.by(Sort.Order.asc("name"), Sort.Order.desc("professionalType"));
    Sort sort = sortBy.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
            : Sort.by(sortBy).descending();
  }

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
