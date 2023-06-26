package com.umulam.fleen.health.service.external.aws;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.*;
import com.umulam.fleen.health.model.dto.mail.EmailTemplateDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TemplateService {

  private final AmazonSimpleEmailService simpleEmailService;

  public TemplateService(AmazonSimpleEmailService simpleEmailService) {
    this.simpleEmailService = simpleEmailService;
  }

  public void createTemplate(EmailTemplateDto dto) {
    Template template = createSesTemplate(dto);

    CreateTemplateRequest request = new CreateTemplateRequest().withTemplate(template);
    CreateTemplateResult result = simpleEmailService.createTemplate(request);

    if (result.getSdkHttpMetadata().getHttpStatusCode() != 200) {
      log.error("{}", result.getSdkResponseMetadata().toString());
    }
  }

  public void updateTemplate(EmailTemplateDto dto) {
    Template template = createSesTemplate(dto);

    UpdateTemplateRequest request = new UpdateTemplateRequest().withTemplate(template);
    UpdateTemplateResult result = simpleEmailService.updateTemplate(request);

    if (result.getSdkHttpMetadata().getHttpStatusCode() != 200) {
      log.error("{}", result.getSdkResponseMetadata().toString());
    }
  }

  public void deleteTemplate(String templateName) {
    DeleteTemplateRequest request = new DeleteTemplateRequest().withTemplateName(templateName);
    DeleteTemplateResult result = simpleEmailService.deleteTemplate(request);

    if (result.getSdkHttpMetadata().getHttpStatusCode() != 200) {
      log.error("{}", result.getSdkResponseMetadata().toString());
    }
  }

  private Template createSesTemplate(EmailTemplateDto dto) {
    Template template = new Template();
    template.setTemplateName(dto.getTemplateName());
    template.setSubjectPart(dto.getSubject());
    template.setHtmlPart(dto.getHtmlText());
    template.setTextPart(dto.getPlainText());
    return template;
  }
}
