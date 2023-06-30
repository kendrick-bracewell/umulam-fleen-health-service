package com.umulam.fleen.health.startup;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.util.ArrayList;
import java.util.List;

import static com.umulam.fleen.health.util.FleenHealthUtil.readResourceFile;

public interface StartupService<T> {

  void seedRecords();

  String getFilePath();

  Class<T> getClazz();

  default List<T> getRecords() throws JsonProcessingException {
    String value = readResourceFile(getFilePath());
    ObjectMapper mapper = new ObjectMapper();
    mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    mapper.findAndRegisterModules();
    CollectionType collectionType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, getClazz());
    return mapper.readValue(value, collectionType);
  }

}
