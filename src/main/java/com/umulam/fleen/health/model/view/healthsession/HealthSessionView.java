package com.umulam.fleen.health.model.view.healthsession;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.umulam.fleen.health.model.view.base.FleenHealthView;
import com.umulam.fleen.health.model.view.member.MemberViewBasic;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalTime;

import static com.umulam.fleen.health.util.DateFormatUtil.*;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HealthSessionView extends FleenHealthView {

  private Long id;
  private String reference;
  private MemberViewBasic patient;
  private MemberViewBasic professional;
  private String comment;
  private String note;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE)
  private LocalDate date;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = TIME)
  private LocalTime time;
  private String timezone;
  private String status;
  private String location;
  private String document;

  @JsonProperty("meeting_link")
  private String meetingLink;

  @JsonProperty("event_link")
  private String eventLink;
}
