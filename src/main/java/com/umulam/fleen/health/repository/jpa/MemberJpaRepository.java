package com.umulam.fleen.health.repository.jpa;

import com.umulam.fleen.health.model.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberJpaRepository extends JpaRepository<Member, Integer> {

  Optional<Member> findByEmailAddress(String emailAddress);

  Optional<Member> findByPhoneNumber(String phoneNumber);
}