package com.umulam.fleen.health.controller;

import com.amazonaws.HttpMethod;
import com.umulam.fleen.health.configuration.aws.s3.S3BucketNames;
import com.umulam.fleen.health.model.response.other.DeleteResponse;
import com.umulam.fleen.health.model.response.other.SignedUrlResponse;
import com.umulam.fleen.health.service.ObjectService;
import com.umulam.fleen.health.service.S3Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "", consumes = { MediaType.APPLICATION_JSON_VALUE })
public class ObjectController {

  private final ObjectService objectService;
  private final S3Service s3Service;
  private final S3BucketNames s3BucketNames;

  public ObjectController(ObjectService objectService,
                          S3Service s3Service,
                          S3BucketNames s3BucketNames) {
    this.objectService = objectService;
    this.s3Service = s3Service;
    this.s3BucketNames = s3BucketNames;
  }

  @GetMapping(value = "/upload/create-signed-url/profile-photo")
  public SignedUrlResponse generateSignedUrlForProfilePhoto(@RequestParam(name = "file_name") String originalFilename) {
    String signedUrl = s3Service.generateSignedUrl(
            s3BucketNames.getMemberDocument(),
            objectService.generateFilename(originalFilename),
            HttpMethod.PUT, 1);
    return new SignedUrlResponse(signedUrl);
  }

  @GetMapping(value = "/create-signed-url/avatar-upload")
  public SignedUrlResponse generateSignedUrlForMemberAvatar(@RequestParam(name = "file_name") String originalFilename) {
    String signedUrl = s3Service.generateSignedUrl(
            s3BucketNames.getProfilePhoto(),
            objectService.generateFilename(originalFilename),
            HttpMethod.PUT, 1);
    return new SignedUrlResponse(signedUrl);
  }

  @DeleteMapping(value = "/delete/member-document")
  public DeleteResponse deleteMemberDocument(@RequestParam(name = "key") String key) {
    return s3Service.deleteObject(s3BucketNames.getMemberDocument(), key);
  }

  @DeleteMapping(value = "/delete/profile-photo")
  public DeleteResponse deleteProfilePhoto(@RequestParam(name = "key") String key) {
    return s3Service.deleteObject(s3BucketNames.getProfilePhoto(), key);
  }

}
