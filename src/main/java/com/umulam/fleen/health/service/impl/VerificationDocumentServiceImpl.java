package com.umulam.fleen.health.service.impl;

import com.umulam.fleen.health.model.domain.Member;
import com.umulam.fleen.health.model.domain.VerificationDocument;
import com.umulam.fleen.health.repository.jpa.VerificationDocumentJpaRepository;
import com.umulam.fleen.health.service.VerificationDocumentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
  public List<VerificationDocument> getByMemberEmailAddress(String emailAddress) {
    return repository.findVerificationDocumentsByEmailAddress(emailAddress);
  }

  @Override
  public Optional<VerificationDocument> getVerificationDocument(Long verificationDocumentId) {
    return repository.findById(verificationDocumentId);
  }

  @Override
  public List<VerificationDocument> getVerificationDocumentsByMember(Member member) {
    return repository.findVerificationDocumentsByMember(member);
  }


  @Override
  @Transactional
  public void saveMany(List<VerificationDocument> verificationDocuments) {
    repository.saveAll(verificationDocuments);
  }

  @Override
  public VerificationDocument getVerificationDocumentByMember(Member member) {
    return repository.findVerificationDocumentByMember(member);
  }
}
