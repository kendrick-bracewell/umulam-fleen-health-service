package com.umulam.fleen.health.model.dto.healthsession;

import lombok.*;

import javax.validation.constraints.Size;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddNoteHealthSessionDto {

  @Size(min = 10, max = 1000, message = "{session.comment.size}")
  private String note;
}
