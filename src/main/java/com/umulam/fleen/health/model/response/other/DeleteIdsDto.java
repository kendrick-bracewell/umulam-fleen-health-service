package com.umulam.fleen.health.model.response.other;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeleteIdsDto {

  @NotEmpty
  private List<Integer> ids;
}
