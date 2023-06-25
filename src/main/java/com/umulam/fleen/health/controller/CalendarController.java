package com.umulam.fleen.health.controller;

import com.umulam.fleen.health.service.CalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(value = "calendar")
public class CalendarController {

  @Autowired
  public CalendarService calendarService;

  @GetMapping(value = "/get")
  public Object hello() {
    calendarService.listEvent();
    return calendarService.createEvent(
            LocalDateTime.of(2023, 6, 25, 0, 5),
            LocalDateTime.of(2023, 6, 25, 0, 10),
            List.of("volunux@gmail.com", "volunuxent@gmail.com")
    );
  }
}
