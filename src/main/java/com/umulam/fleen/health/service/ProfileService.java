package com.umulam.fleen.health.service;

import com.umulam.fleen.health.constant.verification.VerificationDocumentType;
import com.umulam.fleen.health.exception.member.UserNotFoundException;
import com.umulam.fleen.health.model.domain.Member;
import com.umulam.fleen.health.model.domain.VerificationDocument;
import com.umulam.fleen.health.model.request.UpdateVerificationDocumentRequest;
import com.umulam.fleen.health.model.security.FleenUser;
import com.umulam.fleen.health.service.MemberService;
import com.umulam.fleen.health.service.VerificationDocumentService;
import com.umulam.fleen.health.service.impl.S3Service;

import java.util.*;

public interface ProfileService {

  default List<VerificationDocument> setVerificationDocument(List<UpdateVerificationDocumentRequest> updateVerificationDocumentRequest,
                                                             List<VerificationDocument> existingVerificationDocuments) {
    Map<VerificationDocumentType, VerificationDocument> verificationDocumentMap = new HashMap<>();
    for (UpdateVerificationDocumentRequest request: updateVerificationDocumentRequest) {
      if (Objects.nonNull(request.getDocumentLink())) {
        VerificationDocument verificationDocument = VerificationDocument.builder()
                .verificationDocumentType(request.getVerificationDocumentType())
                .filename(getS3Service().getObjectKeyFromUrl(request.getDocumentLink()))
                .link(request.getDocumentLink())
                .build();
        verificationDocumentMap.put(request.getVerificationDocumentType(), verificationDocument);
      }
    }

    for (VerificationDocument existingVerificationDocument: existingVerificationDocuments) {
      VerificationDocument verificationDocument = verificationDocumentMap.get(existingVerificationDocument.getVerificationDocumentType());
      if (Objects.nonNull(verificationDocument)) {
        existingVerificationDocument.setFilename(verificationDocument.getFilename());
        existingVerificationDocument.setLink(verificationDocument.getLink());
        verificationDocumentMap.put(verificationDocument.getVerificationDocumentType(), existingVerificationDocument);
      }
    }
    return new ArrayList<>(verificationDocumentMap.values());
  }

  default void saveVerificationDocument(FleenUser user, List<UpdateVerificationDocumentRequest> request) {
    Member member = getMemberService().getMemberByEmailAddress(user.getEmailAddress());

    if (Objects.isNull(member)) {
      throw new UserNotFoundException(user.getUsername());
    }

    List<VerificationDocument> existingDocuments = getVerificationDocumentService().getVerificationDocumentsByMember(member);
    List<VerificationDocument> newOrUpdatedDocument = setVerificationDocument(request, existingDocuments);
    newOrUpdatedDocument.forEach(document -> document.setMember(member));

    getVerificationDocumentService().saveMany(newOrUpdatedDocument);
  }

  S3Service getS3Service();

  VerificationDocumentService getVerificationDocumentService();

  MemberService getMemberService();
}
