package com.umulam.fleen.health.model.view;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

import static com.umulam.fleen.health.util.DateFormatUtil.DATE_TIME_FORMAT;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CountryView {

  private Integer id;
  private String title;
  private String code;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_FORMAT)
  @JsonProperty("created_on")
  private LocalDateTime createdOn;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_FORMAT)
  @JsonProperty("updated_on")
  private LocalDateTime updatedOn;
}
