package com.umulam.fleen.health.controller;

import com.amazonaws.HttpMethod;
import com.umulam.fleen.health.configuration.aws.s3.S3BucketNames;
import com.umulam.fleen.health.model.response.other.SignedUrlResponse;
import com.umulam.fleen.health.service.impl.S3Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/download",
                consumes = { MediaType.APPLICATION_JSON_VALUE },
                produces = { MediaType.APPLICATION_JSON_VALUE })
public class DownloadController {

  private final S3Service s3Service;
  private final S3BucketNames bucketNames;
  public DownloadController(S3Service s3Service,
                            S3BucketNames bucketNames) {
    this.s3Service = s3Service;
    this.bucketNames = bucketNames;
  }

  @GetMapping(value = "/member/document/{id}")
  public SignedUrlResponse generateMemberDocumentSignedUrl(@PathVariable(name = "id") Integer id) {
    String signedUrl = s3Service.generateSignedUrl(bucketNames.getMemberDocument(), "", HttpMethod.GET, 1);
    return SignedUrlResponse.builder().signedUrl(signedUrl).build();
  }
}
