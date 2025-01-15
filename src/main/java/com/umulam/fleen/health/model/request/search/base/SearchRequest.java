package com.umulam.fleen.health.model.request.search.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

import static com.umulam.fleen.health.constant.base.PagingConstant.*;
import static com.umulam.fleen.health.util.DateFormatUtil.DATE;
import static com.umulam.fleen.health.util.FleenHealthUtil.createPageable;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchRequest {

  @JsonProperty("page_no")
  private Integer pageNo = Integer.valueOf(DEFAULT_PAGE_NUMBER);

  @JsonProperty("page_size")
  private Integer pageSize = Integer.valueOf(DEFAULT_PAGE_SIZE);

  @JsonProperty("sort_dir")
  private String sortDir = DEFAULT_SORT_DIRECTION;

  @JsonProperty("sort_by")
  private String sortBy = DEFAULT_SORT_BY;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE)
  @JsonProperty("start_date")
  private LocalDate startDate;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE)
  @JsonProperty("end_date")
  private LocalDate endDate;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE)
  @JsonProperty("before")
  private LocalDate beforeDate;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE)
  @JsonProperty("after")
  private LocalDate afterDate;

  @JsonIgnore
  private Pageable page;

  public void toPageable() {
    Pageable pageable = createPageable(pageNo, pageSize, sortBy, sortDir);
    this.setPage(pageable);
  }
}
 
