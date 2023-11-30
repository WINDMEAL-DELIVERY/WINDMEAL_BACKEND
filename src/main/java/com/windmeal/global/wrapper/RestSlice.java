package com.windmeal.global.wrapper;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

@JsonIgnoreProperties(ignoreUnknown = true, value = {"pageable"})
public class RestSlice<T> extends SliceImpl<T> {
  @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
  public RestSlice(@JsonProperty("content") List<T> content,
      @JsonProperty("number") int number,
      @JsonProperty("size") int size) {
    super(content, PageRequest.of(number, size), content.size() == size);
  }

  public RestSlice(Slice<T> slice) {
    super(slice.getContent(), slice.getPageable(), slice.hasNext());
  }
}
