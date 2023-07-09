package com.umulam.fleen.health.model.dto.professional;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.umulam.fleen.health.constant.verification.VerificationDocumentType;
import com.umulam.fleen.health.model.request.UpdateVerificationDocumentRequest;
import lombok.*;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

  public List<UpdateVerificationDocumentRequest> toUpdateVerificationDocumentRequest() {
    List<UpdateVerificationDocumentRequest> documents = new ArrayList<>();

    if (Objects.nonNull(educationCertificate)) {
      UpdateVerificationDocumentRequest request = new UpdateVerificationDocumentRequest();
      request.setDocumentLink(educationCertificate);
      request.setVerificationDocumentType(VerificationDocumentType.EDUCATION_CERTIFICATE);
      documents.add(request);
    }

    if (Objects.nonNull(transcript)) {
      UpdateVerificationDocumentRequest request = new UpdateVerificationDocumentRequest();
      request.setDocumentLink(transcript);
      request.setVerificationDocumentType(VerificationDocumentType.TRANSCRIPT);
      documents.add(request);
    }

    if (Objects.nonNull(professionalMembership)) {
      UpdateVerificationDocumentRequest request = new UpdateVerificationDocumentRequest();
      request.setDocumentLink(professionalMembership);
      request.setVerificationDocumentType(VerificationDocumentType.PROFESSIONAL_MEMBERSHIP);
      documents.add(request);
    }

    if (Objects.nonNull(curriculumVitae)) {
      UpdateVerificationDocumentRequest request = new UpdateVerificationDocumentRequest();
      request.setDocumentLink(curriculumVitae);
      request.setVerificationDocumentType(VerificationDocumentType.CURRICULUM_VITAE);
      documents.add(request);
    }
    return documents;
  }
}
