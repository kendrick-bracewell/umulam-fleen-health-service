package com.umulam.fleen.health.controller;

import com.umulam.fleen.health.configuration.aws.s3.S3BucketNames;
import com.umulam.fleen.health.model.response.other.SignedUrlResponse;
import com.umulam.fleen.health.service.ObjectService;
import com.umulam.fleen.health.service.impl.S3Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLConnection;

@Slf4j
@RestController
@RequestMapping(value = "signed-url")

public class SignedUrlController {

  private final ObjectService objectService;
  private final S3Service s3Service;
  private final S3BucketNames s3BucketNames;

  public SignedUrlController(ObjectService objectService,
                          S3Service s3Service,
                          S3BucketNames s3BucketNames) {
    this.objectService = objectService;
    this.s3Service = s3Service;
    this.s3BucketNames = s3BucketNames;
  }

  @GetMapping("/profile-verification-document")
  @PreAuthorize("hasAnyRole('PRE_APPROVED_PROFESSIONAL', 'PRE_APPROVED_BUSINESS', 'PROFESSIONAL', 'BUSINESS', 'ADMINISTRATOR', 'SUPER_ADMINISTRATOR')")
  public SignedUrlResponse forProfileVerificationDocument(@RequestParam(name = "file_name") String fileName) {
    String newFileName = objectService.generateFilename(fileName);
    String signedUrl = s3Service.generateSignedUrl(s3BucketNames.getMemberDocument(), newFileName, URLConnection.guessContentTypeFromName(newFileName));
    return new SignedUrlResponse(signedUrl);
  }

  @GetMapping("/profile-photo")
  @PreAuthorize("hasAnyRole('PRE_APPROVED_PROFESSIONAL', 'PRE_APPROVED_BUSINESS', 'USER', 'PROFESSIONAL', 'BUSINESS', 'ADMINISTRATOR', 'SUPER_ADMINISTRATOR')")
  public SignedUrlResponse forProfilePhoto(@RequestParam(name = "file_name") String fileName) {
    String signedUrl = s3Service.generateSignedUrl(s3BucketNames.getProfilePhoto(), objectService.generateFilename(fileName));
    return new SignedUrlResponse(signedUrl);
  }
}
