package com.umulam.fleen.health.model.dto.member;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.Size;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfilePhotoDto {

  @URL(message = "{member.profilePhoto.isUrl}")
  @Size(max = 500, message = "{member.profilePhoto.size}")
  @JsonProperty("profile_photo")
  private String profilePhoto;
}
