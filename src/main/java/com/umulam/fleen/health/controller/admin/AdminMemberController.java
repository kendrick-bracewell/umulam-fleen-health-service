package com.umulam.fleen.health.controller.admin;

import com.umulam.fleen.health.model.dto.admin.CreateMemberDto;
import com.umulam.fleen.health.model.dto.member.UpdateMemberDetailsDto;
import com.umulam.fleen.health.model.dto.member.UpdateMemberStatusDto;
import com.umulam.fleen.health.model.dto.role.UpdateMemberRoleDto;
import com.umulam.fleen.health.model.request.MemberSearchRequest;
import com.umulam.fleen.health.model.response.FleenHealthResponse;
import com.umulam.fleen.health.model.response.member.UpdateMemberDetailsResponse;
import com.umulam.fleen.health.model.view.MemberView;
import com.umulam.fleen.health.model.view.RoleView;
import com.umulam.fleen.health.model.view.SearchResultView;
import com.umulam.fleen.health.resolver.SearchParam;
import com.umulam.fleen.health.service.admin.AdminMemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.umulam.fleen.health.constant.base.FleenHealthConstant.*;

@Slf4j
@RestController
@RequestMapping(value = "admin/member")
public class AdminMemberController {

  private final AdminMemberService service;

  public AdminMemberController(
          @Qualifier("adminMemberService") AdminMemberService service) {
    this.service = service;
  }

  @GetMapping(value = "/entries")
  public SearchResultView findMembers(@SearchParam MemberSearchRequest request) {
    return service.findMembers(request);
  }

  @GetMapping(value = "/entries/pre-onboarded")
  public SearchResultView findPreOnboardedMembers(@SearchParam MemberSearchRequest request) {
    return service.findPreOnboardedMembers(request);
  }

  @GetMapping(value = "/detail/{id}")
  public MemberView findMemberDetail(@PathVariable(name = "id") Integer memberId) {
    return service.findMemberById(memberId);
  }

  @PostMapping(value = "/create")
  public FleenHealthResponse createMember(@Valid @RequestBody CreateMemberDto dto) {
    service.createMember(dto);
    return new FleenHealthResponse(MEMBER_CREATED);
  }

  @PutMapping(value = "/update/{id}")
  public UpdateMemberDetailsResponse updateProfessionalDetail(
          @PathVariable(name = "id") Integer memberId,
          @Valid @RequestBody UpdateMemberDetailsDto dto) {
    return service.updateMemberDetails(dto, memberId);
  }

  @GetMapping(value = "/update-member-status/{id}")
  public FleenHealthResponse updateMemberProfileStatus(
          @Valid @RequestBody UpdateMemberStatusDto dto,
          @PathVariable(name = "id") Integer memberId) {
    service.updateMemberStatus(dto, memberId);
    return new FleenHealthResponse(MEMBER_STATUS_UPDATED);
  }

  @GetMapping(value = "/update-role/{id}")
  public List<RoleView> getUpdateMemberRole(@PathVariable(name = "id") Integer memberId) {
    return service.getMemberRoles(memberId);
  }

  @PutMapping(value = "/update-role/{id}")
  public FleenHealthResponse updateMemberRole(
          @Valid @RequestBody UpdateMemberRoleDto dto,
          @PathVariable(name = "id") Integer memberId) {
    service.updateMemberRole(dto, memberId);
    return new FleenHealthResponse(MEMBER_ROLE_UPDATED);
  }

  @PutMapping(value = "/resend-onboarding-details/{id}")
  public FleenHealthResponse resendOnboardingDetails(@PathVariable(name = "id") Integer memberId) {
    service.resendOnboardingDetails(memberId);
    return new FleenHealthResponse(RESEND_ONBOARDING_DETAILS);
  }
}
