package com.umulam.fleen.health.controller;

import com.umulam.fleen.health.model.request.search.base.SearchRequest;
import com.umulam.fleen.health.model.security.FleenUser;
import com.umulam.fleen.health.resolver.SearchParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "sessions")
public class MySessionController {

  @GetMapping(value = "/entries")
  public void viewSessions(@AuthenticationPrincipal FleenUser user, @SearchParam SearchRequest searchRequest) {
    
  }
  
  public void viewTherapists() {
    
  } 
  
  public void viewTransactions() {

  }

  public void viewSessionReviews() {

  }

  public void viewPaymentDetails() {

  }
}
