package com.umulam.fleen.health.model.view;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

import static com.umulam.fleen.health.util.DateFormatUtil.DATE_TIME_FORMAT;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BusinessView extends FleenHealthView {

  private String name;
  private String description;

  @JsonProperty("contact_address")
  private String contactAddress;

  @JsonProperty("registration_number_or_id")
  private String registrationNumberOrId;

  private String city;
  private String websiteLink;

  private CountryView country;
  private MemberView member;

  @JsonProperty("verification_documents")
  private List<VerificationDocumentView> verificationDocuments;
}
