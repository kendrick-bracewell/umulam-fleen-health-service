package com.umulam.fleen.health.controller.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "admin/health/session")
public class AdminHealthSessionController {

  @GetMapping(value = "/entries")
  public void viewSessions() {

  }

  @GetMapping(value = "/detail/{id}")
  public void viewSession() {

  }

  @GetMapping(value = "/reviews")
  public void viewSessionReviews() {

  }

  @GetMapping(value = "/transaction")
  public void viewTransactions() {

  }

  @GetMapping(value = "/transaction/{id}")
  public void viewTransaction() {

  }
}
