package com.umulam.fleen.health.service.external.aws;

import com.umulam.fleen.health.exception.base.FleenHealthException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailSendException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

import static com.umulam.fleen.health.constant.ExceptionConstant.FAILED_MAIL_DELIVERY;

@Slf4j
@Component
public class SesService {

  public final MailSender mailSender;

  public SesService(MailSender mailSender) {
    this.mailSender = mailSender;
  }

  public void sendMessage(SimpleMailMessage mailMessage) {
    try {
      this.mailSender.send(mailMessage);
    } catch (MailSendException ex) {
      log.error(ex.getMessage(), ex);
      throw new FleenHealthException(FAILED_MAIL_DELIVERY);
    }
  }
}
