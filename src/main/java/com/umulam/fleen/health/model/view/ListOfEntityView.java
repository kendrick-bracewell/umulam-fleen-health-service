package com.umulam.fleen.health.model.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ListOfEntityView {

  @JsonProperty("page_no")
  private Integer pageNo;

  @JsonProperty("page_size")
  private Integer pageSize;

  @JsonProperty("total_entries")
  private Integer totalEntries;

  @JsonProperty("total_pages")
  private Integer totalPages;

  @JsonProperty("is_last")
  private boolean isLast;

  @JsonProperty("is_first")
  private boolean isFirst;

  private List<Object> values;
}
