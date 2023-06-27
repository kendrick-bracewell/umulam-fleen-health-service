package com.umulam.fleen.health.controller;

import com.umulam.fleen.health.service.external.aws.MobileTextService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value ="mobile-text")
public class MobileTextController {

  private final MobileTextService service;

  public MobileTextController(MobileTextService service) {
    this.service = service;
  }

  @GetMapping
  public boolean test() {
    service.sendSms("2347040643797", "Hello World");
    return true;
  }
}
