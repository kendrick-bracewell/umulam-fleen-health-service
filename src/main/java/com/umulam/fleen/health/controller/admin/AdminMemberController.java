package com.umulam.fleen.health.controller.admin;

import com.umulam.fleen.health.model.dto.member.UpdateMemberStatusDto;
import com.umulam.fleen.health.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "admin/member")
public class AdminMemberController {

  private final MemberService memberService;

  public AdminMemberController(MemberService memberService) {
    this.memberService = memberService;
  }

  @GetMapping(value = "/update-profile-status/{id}")
  public void updateMemberProfileStatus(UpdateMemberStatusDto dto, @PathVariable(name = "id") Integer memberId) {
    memberService.updateMemberStatus(dto, memberId);
  }

  public void updateMemberRole() {

  }
}
