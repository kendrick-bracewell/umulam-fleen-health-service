package com.umulam.fleen.health.service;

import com.amazonaws.HttpMethod;
import com.umulam.fleen.health.configuration.aws.s3.S3BucketNames;
import com.umulam.fleen.health.constant.verification.VerificationDocumentType;
import com.umulam.fleen.health.exception.member.UserNotFoundException;
import com.umulam.fleen.health.model.domain.Member;
import com.umulam.fleen.health.model.domain.VerificationDocument;
import com.umulam.fleen.health.model.request.UpdateVerificationDocumentRequest;
import com.umulam.fleen.health.model.security.FleenUser;
import com.umulam.fleen.health.model.view.VerificationDocumentView;
import com.umulam.fleen.health.service.impl.S3Service;

import java.util.*;

import static java.util.Objects.nonNull;

public interface ProfileService {

  default List<VerificationDocument> setVerificationDocument(List<UpdateVerificationDocumentRequest> updateVerificationDocumentRequest,
                                                             List<VerificationDocument> existingVerificationDocuments) {
    Map<VerificationDocumentType, VerificationDocument> verificationDocumentMap = new HashMap<>();
    for (UpdateVerificationDocumentRequest request: updateVerificationDocumentRequest) {
      if (nonNull(request.getDocumentLink())) {
        VerificationDocument verificationDocument = VerificationDocument.builder()
                .verificationDocumentType(request.getVerificationDocumentType())
                .filename(getS3Service().getObjectKeyFromUrl(request.getDocumentLink()))
                .link(getS3Service().getBaseUrlFromUrl(request.getDocumentLink()))
                .build();
        verificationDocumentMap.put(request.getVerificationDocumentType(), verificationDocument);
      }
    }

    for (VerificationDocument existingVerificationDocument: existingVerificationDocuments) {
      VerificationDocument verificationDocument = verificationDocumentMap.get(existingVerificationDocument.getVerificationDocumentType());
      if (nonNull(verificationDocument)) {
        existingVerificationDocument.setFilename(verificationDocument.getFilename());
        existingVerificationDocument.setLink(verificationDocument.getLink());
        verificationDocumentMap.put(verificationDocument.getVerificationDocumentType(), existingVerificationDocument);
      }
    }
    return new ArrayList<>(verificationDocumentMap.values());
  }

  default List<String> getKeysToDelete(List<UpdateVerificationDocumentRequest> updateVerificationDocumentRequest,
                                       List<VerificationDocument> existingVerificationDocuments) {
    List<String> objectOrKeysToDelete = new ArrayList<>();
    Map<VerificationDocumentType, Object> verificationDocumentMap = new HashMap<>();
    for (UpdateVerificationDocumentRequest request: updateVerificationDocumentRequest) {
      if (nonNull(request.getDocumentLink())) {
        VerificationDocument verificationDocument = VerificationDocument.builder().build();
        verificationDocumentMap.put(request.getVerificationDocumentType(), verificationDocument);
      }
    }

    for (VerificationDocument existingVerificationDocument: existingVerificationDocuments) {
      Object verificationDocument = verificationDocumentMap.get(existingVerificationDocument.getVerificationDocumentType());
      if (nonNull(verificationDocument)) {
        objectOrKeysToDelete.add(existingVerificationDocument.getLink());
      }
    }
    return objectOrKeysToDelete;
  }

  default void saveVerificationDocument(FleenUser user, List<UpdateVerificationDocumentRequest> request) {
    Member member = getMemberService().getMemberByEmailAddress(user.getEmailAddress());

    if (Objects.isNull(member)) {
      throw new UserNotFoundException(user.getUsername());
    }

    List<VerificationDocument> existingDocuments = getVerificationDocumentService().getVerificationDocumentsByMember(member);
    List<VerificationDocument> newOrUpdatedDocument = setVerificationDocument(request, existingDocuments);
    List<String> staleOrUnusedDocumentsToDelete = getKeysToDelete(request, existingDocuments);
    newOrUpdatedDocument.forEach(document -> document.setMember(member));

    getVerificationDocumentService().saveMany(newOrUpdatedDocument);
    getS3Service().deleteMultipleObjects(getS3BucketNames().getMemberDocument(), staleOrUnusedDocumentsToDelete);
  }

  default void generateVerificationDocumentSignedUrl(List<VerificationDocumentView> views) {
    if (nonNull(views) && !views.isEmpty()) {
      views.forEach(document -> {
        document.setLink(getS3Service().generateSignedUrl(getS3BucketNames().getMemberDocument(), document.getFilename(), HttpMethod.GET, 5));
        document.setDownloadLink(getS3Service().generateDownloadUrl(getS3BucketNames().getMemberDocument(), document.getFilename()));
      });
    }
  }

  S3Service getS3Service();

  VerificationDocumentService getVerificationDocumentService();

  MemberService getMemberService();

  S3BucketNames getS3BucketNames();
}
