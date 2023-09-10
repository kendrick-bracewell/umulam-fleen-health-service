package com.umulam.fleen.health.service.impl;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.umulam.fleen.health.exception.ObjectNotFoundException;
import com.umulam.fleen.health.exception.base.FleenHealthException;
import com.umulam.fleen.health.model.response.other.DeleteResponse;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.*;

import static com.umulam.fleen.health.constant.base.FleenHealthConstant.FILE_NOT_FOUND;
import static com.umulam.fleen.health.util.DateTimeUtil.getTimeInMillis;

@Component
public class S3Service {

  private final AmazonS3 amazonS3;
  private static final String FILE_NAME_BLACKLISTED_REGEX = "[^a-zA-Z0-9\\_]";
  private static final String FILE_NAME_SEPARATOR = "_";

  public S3Service(AmazonS3 amazonS3) {
    this.amazonS3 = amazonS3;
  }

  public String generateSignedUrl(String bucketName, String fileName, HttpMethod httpMethod) {
    return generateSignedUrl(bucketName, fileName, httpMethod, 1);
  }

  public String generateSignedUrl(String bucketName, String fileName, HttpMethod httpMethod, int hour) {
    Calendar expirationTime = Calendar.getInstance();
    expirationTime.setTime(new Date());
    expirationTime.add(Calendar.HOUR, hour);
    URL url = amazonS3.generatePresignedUrl(bucketName, fileName, expirationTime.getTime(), httpMethod);
    return url.toString();
  }

  public String generateSignedUrl(String bucketName, String fileName) {
    return generateSignedUrl(bucketName, fileName, HttpMethod.PUT, 1);
  }

  public String generateSignedUrl(String bucketName, String fileName, HttpMethod httpMethod, Date expirationDate) {
    if (Objects.isNull(expirationDate)) {
      expirationDate = new Date();
      long expirationTimeInMillis = expirationDate.getTime();
      expirationTimeInMillis += getTimeInMillis(60, 60, 24, 7);
      expirationDate.setTime(expirationTimeInMillis);
    }

    GeneratePresignedUrlRequest preSignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, fileName);
    preSignedUrlRequest.withExpiration(expirationDate);
    preSignedUrlRequest.withMethod(httpMethod);

    URL url = amazonS3.generatePresignedUrl(preSignedUrlRequest);
    return url.toString();
  }

  public String getObjectSignedUrl(String bucketName, String fileName) {
    if (isObjectExists(bucketName, fileName)) {
      throw new FleenHealthException(FILE_NOT_FOUND);
    }
    return generateSignedUrl(bucketName, fileName, HttpMethod.GET);
  }

  public String generateObjectSignedUrl(String bucketName, String extension) {
    String fileName = UUID.randomUUID().toString() + extension;
    return generateSignedUrl(bucketName, fileName, HttpMethod.PUT);
  }

  public String getObjectKeyFromUrl(@NotNull String objectUrl) {
    String objectKey = objectUrl.substring(objectUrl.lastIndexOf("/") + 1);
    int questionMarkIndex = objectKey.lastIndexOf("?");

    if (questionMarkIndex != -1) {
      objectKey = objectKey.substring(0, questionMarkIndex);
    }
    return objectKey;
  }

  public DeleteResponse deleteObject(@NotNull String bucketName, @NotNull String objectKey) {
    if (isObjectExists(bucketName, objectKey)) {
      DeleteObjectRequest objectRequest = new DeleteObjectRequest(bucketName, objectKey);
      amazonS3.deleteObject(objectRequest);
      return new DeleteResponse();
    }

    throw new ObjectNotFoundException(objectKey);
  }

  public void deleteMultipleObjects(String bucketName, @NotNull List<String> objects){
    DeleteObjectsRequest delObjectsRequests = new DeleteObjectsRequest(bucketName)
            .withKeys(objects.toArray(new String[0]));
    amazonS3.deleteObjects(delObjectsRequests);
  }

  public void moveObject(String bucketSourceName, String objectName, String bucketTargetName) {
    amazonS3.copyObject(
            bucketSourceName, objectName,
            bucketTargetName, objectName);
  }

  private boolean isObjectExists(String bucketName, String fileName) {
    return amazonS3.doesObjectExist(bucketName, fileName);
  }

  public String generateObjectKey(String objectName) {
    return (new Date()).getTime() +
            "-" +
            objectName.replaceAll(FILE_NAME_BLACKLISTED_REGEX, FILE_NAME_SEPARATOR);
  }

  public Object getObjectStream(String bucketName, String filename) {
    S3Object object = getObject(bucketName, filename);
    return ResponseEntity
            .ok()
            .cacheControl(CacheControl.noCache())
            .header("Content-type", MediaType.APPLICATION_OCTET_STREAM_VALUE)
            .header("Content-disposition", "attachment; filename=".concat(filename))
            .body(new InputStreamResource(object.getObjectContent()));
  }

  public S3Object getObject(String bucketName, String filename) {
    if (isObjectExists(bucketName, filename)) {
      return amazonS3.getObject(bucketName, filename);
    }
    return null;
  }

  @Async
  public void uploadObject(String bucketName, MultipartFile multipartFile, Optional<Map<String, String>> objectMetaData) {
    try {
      File file = convertMultipartFileToFile(multipartFile);
      String fileName = LocalDateTime.now().toString().concat("_").concat(file.getName());

      PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName, file);
      putObjectRequest.setMetadata(new ObjectMetadata());
      putObjectRequest.setCannedAcl(CannedAccessControlList.PublicRead);

      ObjectMetadata objectMetadata = new ObjectMetadata();
      objectMetaData.ifPresent(map -> {
        if (!map.isEmpty()) {
          map.forEach(objectMetadata::addUserMetadata);
        }
      });

      amazonS3.putObject(putObjectRequest);
      Files.delete(file.getAbsoluteFile().toPath());
    }
    catch (Exception e) {
      throw new RuntimeException("Error processing file upload");
    }
  }

  private File convertMultipartFileToFile(final MultipartFile multipartFile) {
    String fileName = Objects.requireNonNull(multipartFile.getOriginalFilename());
    final File file = new File(fileName);

    try (final FileOutputStream outputStream = new FileOutputStream(file)) {
      outputStream.write(multipartFile.getBytes());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return file;
  }

}
