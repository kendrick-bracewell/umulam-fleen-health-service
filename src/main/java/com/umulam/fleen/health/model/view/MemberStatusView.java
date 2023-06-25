package com.umulam.fleen.health.model.view;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberStatusView {

  private Integer id;
  private String title;
  private String code;
  private String description;
  private LocalDateTime createdOn;
  private LocalDateTime updatedOn;
}
