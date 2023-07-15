package com.umulam.fleen.health.model.dto.business;

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
public class UploadBusinessDocumentDto {

  @URL(message = "{verification.document.isUrl}")
  @Size(max = 500, message = "{verification.document.size}")
  @JsonProperty("registration_document")
  private String registrationDocument;

  @URL(message = "{verification.document.isUrl}")
  @Size(max = 500, message = "{verification.document.size}")
  @JsonProperty("tax_identification")
  private String taxIdentification;

  @URL(message = "{verification.document.isUrl}")
  @Size(max = 500, message = "{verification.document.size}")
  private String passport;

  @URL(message = "{verification.document.isUrl}")
  @Size(max = 500, message = "{verification.document.size}")
  @JsonProperty("driver_license")
  private String driverLicense;

  @URL(message = "{verification.document.isUrl}")
  @Size(max = 500, message = "{verification.document.size}")
  @JsonProperty("national_id")
  private String nationalId;

  @URL(message = "{verification.document.isUrl}")
  @Size(max = 500, message = "{verification.document.size}")
  @JsonProperty("lease_agreement")
  private String leaseAgreement;

  @URL(message = "{verification.document.isUrl}")
  @Size(max = 500, message = "{verification.document.size}")
  @JsonProperty("utility_bill")
  private String utilityBill;

  public List<UpdateVerificationDocumentRequest> toUpdateVerificationDocumentRequest() {
    List<UpdateVerificationDocumentRequest> documents = new ArrayList<>();

    if (nonNull(registrationDocument)) {
      documents.add(createNew(registrationDocument, BUSINESS_REGISTRATION_DOCUMENT));
    }

    if (nonNull(taxIdentification)) {
      documents.add(createNew(taxIdentification, BUSINESS_TAX_IDENTIFICATION));
    }

    if (nonNull(passport)) {
      documents.add(createNew(passport, PASSPORT));
    }

    if (nonNull(nationalId)) {
      documents.add(createNew(nationalId, NATIONAL_ID_CARD));
    }

    if (nonNull(driverLicense)) {
      documents.add(createNew(driverLicense, DRIVER_LICENSE));
    }

    if (nonNull(leaseAgreement)) {
      documents.add(createNew(leaseAgreement, LEASE_AGREEMENT));
    }

    if (nonNull(utilityBill)) {
      documents.add(createNew(utilityBill, UTILITY_BILL));
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
