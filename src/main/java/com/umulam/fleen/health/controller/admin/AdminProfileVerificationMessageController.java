package com.umulam.fleen.health.controller.admin;

import com.umulam.fleen.health.model.response.profileverificationmessage.GetProfileVerificationMessages;
import com.umulam.fleen.health.service.ProfileVerificationMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "admin/profile-verification-message")
public class AdminProfileVerificationMessageController {
  
  private final ProfileVerificationMessageService service;
  
  public AdminProfileVerificationMessageController(ProfileVerificationMessageService service) {
    this.service = service;
  }
  
  @GetMapping(value = "/titles")
  public List<GetProfileVerificationMessages> getProfileVerificationMessageTitles() {
    return service.getTitles();
  }
}
