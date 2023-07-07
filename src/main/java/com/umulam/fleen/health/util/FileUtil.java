package com.umulam.fleen.health.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.umulam.fleen.health.exception.util.FileUtilException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ResourceUtils;

import javax.validation.constraints.NotEmpty;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.stream.Collectors;

/**
 * The FileUtils contains method and implementations for reading file data, deserializing of Java object from JSON file and also
 * returning file data in form of stream.
 *
 * @author Alperen Oezkan
 * @author Yusuf Alamu Musa
 */
@Slf4j
public class FileUtil {

  private FileUtil() {
  }

  /**
   * This method accepts a file path, verifies that the file exists and returns the file content as a string
   *
   * @param path the path or directory to the file
   * @return the file content as string
   * @throw FileUtilException if an error occurred while opening or processing the file
   */
  public static String readAsString(@NotEmpty String path) {
    File file = new File(path);
    try {
      return Files.readString(file.toPath());
    } catch (IOException e) {
      String message = String.format("An error occurred inside readAsString method while reading file content with path=%s : %s",
          path,
          e.getMessage());
      log.error(message);
      throw new FileUtilException(message);
    }
  }


  /**
   * This method accepts the path to a file whose contents is in JSON format and the class to deserialize the JSON content to
   *
   * @param path  the path or directory to the file
   * @param clazz the class to deserialize the content of the JSON from
   * @return an object containing values based on the structure of the JSON content
   * @throw FileUtilException if an error occurred while opening or processing the file
   */
  public static <T> T getObjectFromJsonFileByClassPath(@NotEmpty String path, Class<T> clazz) {
    try {
      File file = ResourceUtils.getFile("classpath:".concat(path));
      ObjectMapper mapper = new ObjectMapper();
      mapper.registerModule(new JavaTimeModule());
      mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
      mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
      return mapper.readValue(file, clazz);
    } catch (IOException e) {
      String message = String.format("An error occurred while reading file content with path=%s : %s", path, e.getMessage());
      log.error(message);
      throw new FileUtilException(message);
    }
  }

  /**
   * This method accepts a path to a file and return the content of the file as string
   *
   * @param path the path or directory to the file
   * @return a string containing the stream content of the file
   * @throw FileUtilException if an error occurred while opening or processing the file
   */
  public static String readAsStream(@NotEmpty String path) {
    InputStream inputStream = FileUtil.class.getClassLoader().getResourceAsStream(path);
    if (inputStream == null) {
      String message = String.format("An error occurred inside readAsStream method while reading file content with path=%s",
          path);
      log.error(message);
      throw new FileUtilException(message);
    } else {
      return new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)).lines()
          .collect(Collectors.joining("\n"));
    }
  }
}
