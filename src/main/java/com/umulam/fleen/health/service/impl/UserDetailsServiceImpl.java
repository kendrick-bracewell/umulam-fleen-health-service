package com.umulam.fleen.health.service.impl;

import com.umulam.fleen.health.model.domain.Member;
import com.umulam.fleen.health.model.security.FleenUser;
import com.umulam.fleen.health.repository.jpa.MemberJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Primary
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

  private final MemberJpaRepository repository;

  @Override
  @Transactional(readOnly = true)
  public UserDetails loadUserByUsername(String emailAddress) throws UsernameNotFoundException {

    Member member = repository
            .findByEmailAddress(emailAddress)
            .orElseThrow(() -> new UsernameNotFoundException(emailAddress));

    return FleenUser.fromMember(member);
  }

}
