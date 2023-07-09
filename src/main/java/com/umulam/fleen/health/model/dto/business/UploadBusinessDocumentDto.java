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

    if (Objects.nonNull(registrationDocument)) {
      UpdateVerificationDocumentRequest request = new UpdateVerificationDocumentRequest();
      request.setDocumentLink(registrationDocument);
      request.setVerificationDocumentType(VerificationDocumentType.BUSINESS_REGISTRATION_DOCUMENT);
      documents.add(request);
    }

    if (Objects.nonNull(taxIdentification)) {
      UpdateVerificationDocumentRequest request = new UpdateVerificationDocumentRequest();
      request.setDocumentLink(taxIdentification);
      request.setVerificationDocumentType(VerificationDocumentType.BUSINESS_TAX_IDENTIFICATION);
      documents.add(request);
    }

    if (Objects.nonNull(passport)) {
      UpdateVerificationDocumentRequest request = new UpdateVerificationDocumentRequest();
      request.setDocumentLink(passport);
      request.setVerificationDocumentType(VerificationDocumentType.PASSPORT);
      documents.add(request);
    }

    if (Objects.nonNull(driverLicense)) {
      UpdateVerificationDocumentRequest request = new UpdateVerificationDocumentRequest();
      request.setDocumentLink(driverLicense);
      request.setVerificationDocumentType(VerificationDocumentType.DRIVER_LICENSE);
      documents.add(request);
    }

    if (Objects.nonNull(nationalId)) {
      UpdateVerificationDocumentRequest request = new UpdateVerificationDocumentRequest();
      request.setDocumentLink(nationalId);
      request.setVerificationDocumentType(VerificationDocumentType.NATIONAL_ID_CARD);
      documents.add(request);
    }

    if (Objects.nonNull(leaseAgreement)) {
      UpdateVerificationDocumentRequest request = new UpdateVerificationDocumentRequest();
      request.setDocumentLink(leaseAgreement);
      request.setVerificationDocumentType(VerificationDocumentType.LEASE_AGREEMENT);
      documents.add(request);
    }

    if (Objects.nonNull(utilityBill)) {
      UpdateVerificationDocumentRequest request = new UpdateVerificationDocumentRequest();
      request.setDocumentLink(utilityBill);
      request.setVerificationDocumentType(VerificationDocumentType.UTILITY_BILL);
      documents.add(request);
    }
    return documents;
  }
}
