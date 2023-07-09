package com.umulam.fleen.health.repository.jpa;

import com.umulam.fleen.health.model.domain.Member;
import com.umulam.fleen.health.model.domain.VerificationDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface VerificationDocumentJpaRepository extends JpaRepository<VerificationDocument, Integer> {

  @Query("SELECT vd FROM VerificationDocument vd WHERE vd.member.emailAddress = :emailAddress")
  Optional<VerificationDocument> findVerificationDocumentByEmailAddress(String emailAddress);


  List<VerificationDocument> findVerificationDocumentByMember(Member member);
}
