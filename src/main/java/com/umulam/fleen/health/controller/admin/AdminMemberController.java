package com.umulam.fleen.health.controller.admin;

import com.umulam.fleen.health.model.dto.member.UpdateMemberStatusDto;
import com.umulam.fleen.health.model.response.FleenHealthResponse;
import com.umulam.fleen.health.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.umulam.fleen.health.constant.base.FleenHealthConstant.MEMBER_STATUS_UPDATED;

@Slf4j
@RestController
@RequestMapping(value = "admin/member")
public class AdminMemberController {

  private final MemberService memberService;

  public AdminMemberController(MemberService memberService) {
    this.memberService = memberService;
  }

  @GetMapping(value = "/update-member-status/{id}")
  public FleenHealthResponse updateMemberProfileStatus(
          @Valid @RequestBody UpdateMemberStatusDto dto,
          @PathVariable(name = "id") Integer memberId) {
    memberService.updateMemberStatus(dto, memberId);
    return new FleenHealthResponse(MEMBER_STATUS_UPDATED);
  }

  @GetMapping(value = "/update-role/{id}")
  public void updateMemberRole(@PathVariable(name = "id") Integer memberId) {

  }
}
