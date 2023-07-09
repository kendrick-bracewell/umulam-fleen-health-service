package com.umulam.fleen.health.service.impl;

import com.umulam.fleen.health.model.domain.Member;
import com.umulam.fleen.health.model.domain.VerificationDocument;
import com.umulam.fleen.health.repository.jpa.VerificationDocumentJpaRepository;
import com.umulam.fleen.health.service.VerificationDocumentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class VerificationDocumentServiceImpl implements VerificationDocumentService {

  private final VerificationDocumentJpaRepository repository;

  public VerificationDocumentServiceImpl(VerificationDocumentJpaRepository repository) {
    this.repository = repository;
  }

  @Override
  public Optional<VerificationDocument> getVerificationDocument(Integer verificationDocumentId) {
    return repository.findById(verificationDocumentId);
  }

  @Override
  public List<VerificationDocument> getVerificationDocumentsByMember(Member member) {
    return repository.findVerificationDocumentByMember(member);
  }
}
