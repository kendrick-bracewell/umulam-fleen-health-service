package com.umulam.fleen.health.model.dto.mail;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmailTemplateDto {

  @NotBlank
  @JsonProperty(value = "template_name")
  private String templateName;

  @NotBlank
  private String subject;

  @NotBlank
  private String htmlText;

  @NotBlank
  private String plainText;



}
 
