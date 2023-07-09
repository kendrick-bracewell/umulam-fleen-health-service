package com.umulam.fleen.health.controller.admin;

import com.umulam.fleen.health.service.BusinessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "admin/business")
public class AdminBusinessController {

  private final BusinessService businessService;

  public AdminBusinessController(BusinessService businessService) {
    this.businessService = businessService;
  }

  @GetMapping(value = "entries")
  public List<Object> findEntries() {
    return businessService.getBusinessesByAdmin();
  }
}
