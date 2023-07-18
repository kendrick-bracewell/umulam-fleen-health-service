package com.umulam.fleen.health.model.view.search;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SearchResultView {

  @JsonProperty("page_no")
  private Integer pageNo;

  @JsonProperty("page_size")
  private Integer pageSize;

  @JsonProperty("total_entries")
  private Long totalEntries;

  @JsonProperty("total_pages")
  private Integer totalPages;

  @JsonProperty("is_last")
  private boolean isLast;

  @JsonProperty("is_first")
  private boolean isFirst;

  private List<?> values;


}
