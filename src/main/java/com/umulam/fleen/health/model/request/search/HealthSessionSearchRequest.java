package com.umulam.fleen.health.model.request.search;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.umulam.fleen.health.constant.session.HealthSessionStatus;
import com.umulam.fleen.health.constant.session.SessionLocation;
import com.umulam.fleen.health.model.request.search.base.SearchRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalTime;

import static com.umulam.fleen.health.util.DateFormatUtil.DATE;
import static com.umulam.fleen.health.util.DateFormatUtil.TIME;

@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HealthSessionSearchRequest extends SearchRequest {

  private String reference;
  private String timezone;
  private SessionLocation location;

  @JsonProperty("session_status")
  private HealthSessionStatus sessionStatus;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE)
  private LocalDate date;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = TIME)
  private LocalTime time;

}
