package com.windmeal.store.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@Schema(title = "가게 정보 (CMS)")
public class CategoryStoreMenuResponse {

  private List<StoreCategoryResponse> storeCategoryResponse;
  private StoreMenuResponse storeResponse;


  public static CategoryStoreMenuResponse of(List<StoreCategoryResponse> storeCategoryResponse,
      StoreMenuResponse storeResponse){
    return new CategoryStoreMenuResponse(storeCategoryResponse,storeResponse);
  }
  private CategoryStoreMenuResponse(List<StoreCategoryResponse> storeCategoryResponse,
      StoreMenuResponse storeResponse) {
    this.storeCategoryResponse = storeCategoryResponse;
    this.storeResponse = storeResponse;
  }
}
