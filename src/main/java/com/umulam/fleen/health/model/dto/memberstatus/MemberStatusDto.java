package com.umulam.fleen.health.model.dto.memberstatus;

import com.umulam.fleen.health.model.domain.MemberStatus;
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
public class MemberStatusDto {

  @NotBlank(message = "{memberStatus.title.notEmpty}")
  @Size(min = 1, max = 200, message = "{memberStatus.title.size}")
  private String title;

  @Size(max = 500, message = "{memberStatus.description.size}")
  private String description;

  public MemberStatus toMemberStatus() {
    String code = Arrays
            .stream(title.split(" "))
            .map(String::trim)
            .map(String::toUpperCase)
            .collect(Collectors.joining("_"));

    return MemberStatus.builder()
            .title(StringUtils.capitalize(title))
            .code(code)
            .description(description)
            .build();
  }
}
