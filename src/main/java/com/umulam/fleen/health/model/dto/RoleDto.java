package com.umulam.fleen.health.model.dto;

import com.umulam.fleen.health.model.domain.Role;
import lombok.*;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Arrays;
import java.util.stream.Collectors;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleDto {

  @NotBlank(message = "{role.title.notEmpty}")
  @Size(min = 1, max = 200, message = "{role.title.size}")
  private String title;

  @Size(max = 500, message = "{role.description.size}")
  private String description;

  public Role toRole() {
    String code = Arrays
            .stream(title.split(" "))
            .map(String::trim)
            .map(String::toUpperCase)
            .collect(Collectors.joining("_"));

    return Role.builder()
            .title(StringUtils.capitalize(title))
            .code(code)
            .description(description)
            .build();
  }
}
