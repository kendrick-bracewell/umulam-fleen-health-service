package com.umulam.fleen.health.model.dto.member;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMemberDetailsDto {

  @NotBlank(message = "{member.firstName.notEmpty}")
  @Size(min = 2, max = 5, message = "{member.firstName.size}")
  @JsonProperty("first_name")
  private String firstName;

  @NotBlank(message = "{member.lastName.notEmpty}")
  @Size(min = 2, max = 5, message = "{member.lastName.size}")
  @JsonProperty("last_name")
  private String lastName;
}
