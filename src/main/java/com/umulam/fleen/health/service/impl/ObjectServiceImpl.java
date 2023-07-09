package com.umulam.fleen.health.service.impl;

import com.umulam.fleen.health.service.ObjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Objects;

@Slf4j
@Service
public class ObjectServiceImpl implements ObjectService {

  private final S3Service s3Service;

  public ObjectServiceImpl(S3Service s3Service) {
    this.s3Service = s3Service;
  }

  @Override
  public String getFileExtension(String filename) {
    return StringUtils.getFilenameExtension(filename);
  }

  @Override
  public String stripExtension(String filename) {
    return StringUtils.stripFilenameExtension(filename);
  }

  @Override
  public String generateFilename(String filename) {
    String fileExt = getFileExtension(filename);
    return s3Service
            .generateObjectKey(stripExtension(filename))
            .concat(".")
            .concat(!(Objects.isNull(fileExt)) ? fileExt.toLowerCase() : "");
  }
}
