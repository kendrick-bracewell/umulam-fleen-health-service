package com.umulam.fleen.health.model.view.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

import static com.umulam.fleen.health.util.DateFormatUtil.DATE_TIME;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FleenHealthView {

  private Integer id;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME)
  @JsonProperty("created_on")
  private LocalDateTime createdOn;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME)
  @JsonProperty("updated_on")
  private LocalDateTime updatedOn;
}
