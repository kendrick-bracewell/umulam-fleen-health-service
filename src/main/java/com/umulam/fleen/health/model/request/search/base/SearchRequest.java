package com.umulam.fleen.health.model.request.search.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;

import static com.umulam.fleen.health.constant.base.PagingConstant.*;
import static com.umulam.fleen.health.util.DateFormatUtil.DATE;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchRequest {

  @Builder.Default
  @JsonProperty("page_no")
  private Integer pageNo = Integer.valueOf(DEFAULT_PAGE_NUMBER);

  @Builder.Default
  @JsonProperty("page_size")
  private Integer pageSize = Integer.valueOf(DEFAULT_PAGE_SIZE);

  @Builder.Default
  @JsonProperty("sort_dir")
  private String sortDir = DEFAULT_SORT_DIRECTION;

  @Builder.Default
  @JsonProperty("sort_by")
  private String sortBy = DEFAULT_SORT_BY;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE)
  @JsonProperty("created_on")
  private LocalDate createdOn;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE)
  @JsonProperty("updated_on")
  private LocalDate updatedOn;

  @JsonIgnore
  private PageRequest pageRequest;
}
