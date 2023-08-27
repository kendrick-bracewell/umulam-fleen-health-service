package com.umulam.fleen.health.util;

import com.umulam.fleen.health.model.view.search.SearchResultView;
import com.umulam.fleen.health.service.impl.UserDetailsServiceImpl;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.beans.FeatureDescriptor;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Component
@AllArgsConstructor
public class FleenHealthUtil {

  private final UserDetailsServiceImpl userDetailsService;
  private final JwtProvider jwtProvider;

  public static String[] getNullPropertyNames(Object source) {
    final BeanWrapper wrappedSource = new BeanWrapperImpl(source);

    return Stream.of(wrappedSource.getPropertyDescriptors())
                .map(FeatureDescriptor::getName)
                .filter(propertyName -> {
                    try {
                      return Objects.isNull(wrappedSource.getPropertyValue(propertyName));
                    } catch (Exception e) {
                      return false;
                    }
                })
                .toArray(String[]::new);
  }

  public static String readResourceFile(String path) {
    ResourceLoader resourceLoader = new DefaultResourceLoader();
    Resource resource = resourceLoader.getResource(path);
    return asString(resource);
  }

  private static String asString(Resource resource) {
    try (Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)) {
      return FileCopyUtils.copyToString(reader);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  public static Pageable createPageable(int pageNo, int pageSize, String sortBy, String sortDir) {
    Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                  ? Sort.by(sortBy).ascending()
                  : Sort.by(sortBy).descending();
    return PageRequest.of(pageNo, pageSize, sort);
  }

  public static boolean areNotEmpty(Object...args) {
    if (ArrayUtils.isEmpty(args)) {
      return false;
    } else {
      long arrLength = args.length;
      long nonEmptyElements = Arrays
              .stream(args)
              .filter(arg -> arg instanceof String
                ? StringUtils.isNotBlank((String) arg)
                : Objects.nonNull(arg))
              .count();
      return arrLength == nonEmptyElements;
    }
  }

  public static SearchResultView toSearchResult(List<?> values, Page<?> page) {
    if (page != null) {
      return SearchResultView.builder()
        .isFirst(page.isFirst())
        .isLast(page.isLast())
        .totalPages(page.getTotalPages())
        .totalEntries(page.getTotalElements())
        .pageNo(page.getNumber())
        .pageSize(page.getSize())
        .values(values)
        .build();
    }
    return SearchResultView.builder()
      .values(values)
      .build();
  }

}
