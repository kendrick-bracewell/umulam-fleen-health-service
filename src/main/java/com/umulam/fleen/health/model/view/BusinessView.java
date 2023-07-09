package com.umulam.fleen.health.model.view;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.umulam.fleen.health.model.view.CountryView;
import com.umulam.fleen.health.model.view.MemberView;
import com.umulam.fleen.health.model.view.VerificationDocumentView;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

import static com.umulam.fleen.health.util.DateFormatUtil.DATE_TIME_FORMAT;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BusinessView {

  private Integer id;
  private String name;
  private String description;
  private String contactAddress;
  private String registrationNumberOrId;
  private String city;
  private String websiteLink;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_FORMAT)
  private LocalDateTime createdOn;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_FORMAT)
  private LocalDateTime updatedOn;

  private CountryView country;
  private MemberView member;
  private List<VerificationDocumentView> verificationDocuments;
}
