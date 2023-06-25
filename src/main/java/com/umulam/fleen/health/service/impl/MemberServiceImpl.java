package com.umulam.fleen.health.service.impl;

import com.umulam.fleen.health.model.domain.Member;
import com.umulam.fleen.health.model.domain.Role;
import com.umulam.fleen.health.model.dto.authentication.SignUpDto;
import com.umulam.fleen.health.repository.jpa.MemberJpaRepository;
import com.umulam.fleen.health.service.MemberService;
import com.umulam.fleen.health.service.RoleService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class MemberServiceImpl implements MemberService {

  private final MemberJpaRepository memberJpaRepository;

  private final RoleService roleService;
  private final PasswordEncoder passwordEncoder;


  public MemberServiceImpl(MemberJpaRepository memberJpaRepository,
                           RoleService roleService,
                           PasswordEncoder passwordEncoder) {
    this.memberJpaRepository = memberJpaRepository;
    this.roleService = roleService;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  @Transactional
  public Member signup(SignUpDto dto) {
    Role role = roleService.getRoleByCode(null);
    Member member = dto.toMember();
    String passwordHash = passwordEncoder.encode(member.getPassword());
    member.setPassword(passwordHash);
    member.addRole(role);
    return memberJpaRepository.save(member);
  }

  @Override
  public boolean isMemberExists(@NonNull String emailAddress) {
    return memberJpaRepository.findByEmailAddress(emailAddress).isPresent();
  }
}
