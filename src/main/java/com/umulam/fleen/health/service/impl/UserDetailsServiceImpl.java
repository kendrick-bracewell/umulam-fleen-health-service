package com.umulam.fleen.health.service.impl;

import com.umulam.fleen.health.model.domain.Member;
import com.umulam.fleen.health.model.security.UserDetailsImpl;
import com.umulam.fleen.health.repository.jpa.MemberJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
@Primary
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

  private final MemberJpaRepository repository;

  @Override
  @Transactional(readOnly = true)
  public UserDetails loadUserByUsername(String emailAddress) throws UsernameNotFoundException {

    if (Objects.isNull(emailAddress)) {
      throw new UsernameNotFoundException("Unknown");
    }

    Optional<Member> member = repository.findByEmailAddress(emailAddress);

    if (member.isEmpty()) {
      throw new UsernameNotFoundException(emailAddress);
    }

    return UserDetailsImpl.build(member.get());
  }


}
