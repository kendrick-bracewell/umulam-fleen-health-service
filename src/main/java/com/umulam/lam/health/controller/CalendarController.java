package com.umulam.lam.health.controller;

import com.umulam.lam.health.service.CalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "calendar")
public class CalendarController {

  @Autowired
  public CalendarService calendarService;

  @GetMapping(value = "/get")
  public String hello() {
    calendarService.listEvent();
    return "Hello World";
  }
}
