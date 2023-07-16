package com.umulam.fleen.health.controller.admin;

import com.umulam.fleen.health.model.dto.profileverificationmessage.ProfileVerificationMessageDto;
import com.umulam.fleen.health.model.response.FleenHealthResponse;
import com.umulam.fleen.health.model.response.other.DeleteIdsDto;
import com.umulam.fleen.health.model.response.other.DeleteResponse;
import com.umulam.fleen.health.model.response.profileverificationmessage.GetProfileVerificationMessages;
import com.umulam.fleen.health.service.ProfileVerificationMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.umulam.fleen.health.constant.base.FleenHealthConstant.PROFILE_VERIFICATION_MESSAGE_SAVED;

@Slf4j
@RestController
@RequestMapping(value = "admin/profile-verification-message")
public class AdminProfileVerificationMessageController {
  
  private final ProfileVerificationMessageService service;
  
  public AdminProfileVerificationMessageController(ProfileVerificationMessageService service) {
    this.service = service;
  }
  
  @GetMapping(value = "/titles")
  public List<GetProfileVerificationMessages> getProfileVerificationMessageTitles() {
    return service.getBasicDetails();
  }

  @PostMapping(value = "/save")
  public FleenHealthResponse save(@Valid @RequestBody ProfileVerificationMessageDto dto) {
    service.saveProfileVerificationMessage(dto);
    return new FleenHealthResponse(PROFILE_VERIFICATION_MESSAGE_SAVED);
  }

  @PutMapping(value ="/update/{id}")
  public FleenHealthResponse updateProfileVerificationMessage(
          @PathVariable(name = "id") Integer id,
          @Valid @RequestBody ProfileVerificationMessageDto dto) {
    service.updateProfileVerificationMessage(id, dto);
    return new FleenHealthResponse(PROFILE_VERIFICATION_MESSAGE_SAVED);
  }

  @DeleteMapping(value ="/delete-many")
  public DeleteResponse deleteMany(@Valid @RequestBody DeleteIdsDto ids) {
    service.deleteMany(ids);
    return new DeleteResponse();
  }
}
