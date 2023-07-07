package com.umulam.fleen.health.service.external.google;

import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.AutoForwarding;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
//@Component
public class GmailService {

  private final Gmail service;

  public GmailService(Gmail service) {
    this.service = service;
  }

  public Object enableEmailForwarding(String alias, String primaryEmail) {
    try {
      AutoForwarding autoForwarding = new AutoForwarding();
      autoForwarding.setEmailAddress(primaryEmail);
      autoForwarding.setEnabled(true);
      return service.users().settings().updateAutoForwarding(alias, autoForwarding).execute();
    } catch (IOException ex) {
      log.error(ex.getMessage(), ex);
    }
    return null;
  }
}
