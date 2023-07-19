package com.umulam.fleen.health.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "mail")
public class EmailController {

/*  public final EmailServiceImpl emailService;
  private final DirectoryService directoryService;
  private final GmailService gmailService;

  public EmailController(EmailServiceImpl emailService,
                         DirectoryService directoryService,
                         GmailService gmailService) {
    this.emailService = emailService;
    this.directoryService = directoryService;
    this.gmailService = gmailService;
  }

  @GetMapping
  public boolean testEmail() {
    EmailDetails details = EmailDetails.builder()
            .from("volunux@gmail.com")
            .to("volunux@gmail.com")
            .subject("Hello World")
            .body("Hello World Yusuf Alamu Musa")
            .build();
    return emailService.sendMessage(details);
  }

  @GetMapping(value = "/user")
  public Object user() {
    return directoryService.createUser("volunux@gmail.com");
  }

  @GetMapping(value = "/alias")
  public Object alias() {
    return directoryService.createEmailAlias("yusebobo@volunux.com", "Yusuf", "Musa");
  }

  @GetMapping(value = "/forward")
  public Object forward() {
    return gmailService.enableEmailForwarding("umulam@volunux.com", "volunux@gmail.com");
  }*/
}
