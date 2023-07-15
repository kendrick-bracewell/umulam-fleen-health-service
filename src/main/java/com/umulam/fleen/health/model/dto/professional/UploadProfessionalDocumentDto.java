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

import static com.umulam.fleen.health.constant.verification.VerificationDocumentType.*;
import static java.util.Objects.nonNull;

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

    if (nonNull(educationCertificate)) {
      documents.add(createNew(educationCertificate, EDUCATION_CERTIFICATE));
    }

    if (nonNull(transcript)) {
      documents.add(createNew(transcript, TRANSCRIPT));
    }

    if (nonNull(professionalMembership)) {
      documents.add(createNew(professionalMembership, PROFESSIONAL_MEMBERSHIP));
    }

    if (nonNull(curriculumVitae)) {
      documents.add(createNew(curriculumVitae, CURRICULUM_VITAE));
    }
    return documents;
  }

  private UpdateVerificationDocumentRequest createNew(String link, VerificationDocumentType documentType) {
    UpdateVerificationDocumentRequest request = new UpdateVerificationDocumentRequest();
    request.setDocumentLink(link);
    request.setVerificationDocumentType(documentType);
    return request;
  }
}
