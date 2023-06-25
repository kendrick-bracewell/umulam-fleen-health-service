package com.umulam.fleen.health.service;

public interface ObjectService {
  String getFileExtension(String filename);

  String stripExtension(String filename);

  String generateFilename(String filename);
}
