package com.umulam.fleen.health.model.dto.professional;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.Size;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UploadProfessionalDocumentDto {

  @URL(message = "{verification.document.isUrl}")
  @Size(max = 500, message = "{verification.document.size}")
  @JsonProperty("education_certificate")
  private String educationCertificate;

  @URL(message = "{verification.document.isUrl}")
  @Size(max = 500, message = "{verification.document.size}")
  private String transcript;

  @URL(message = "{verification.document.isUrl}")
  @Size(max = 500, message = "{verification.document.size}")
  @JsonProperty("professional_membership")
  private String professionalMembership;

  @URL(message = "{verification.document.isUrl}")
  @Size(max = 500, message = "{verification.document.size}")
  @JsonProperty("curriculum_vitae")
  private String curriculumVitae;
}
