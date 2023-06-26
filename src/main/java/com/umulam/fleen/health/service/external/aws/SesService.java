package com.umulam.fleen.health.service.external.aws;

import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

@Component
public class SesService {

  public final MailSender mailSender;

  public SesService(MailSender mailSender) {
    this.mailSender = mailSender;
  }

  public void sendMessage(SimpleMailMessage mailMessage) {
    this.mailSender.send(mailMessage);
  }
}
