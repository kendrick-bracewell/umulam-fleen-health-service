package com.umulam.fleen.health.model.dto.role;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMemberRoleDto {

  @NotEmpty(message = "{entity.ids.notEmpty}")
  private List<Long> ids;
}
