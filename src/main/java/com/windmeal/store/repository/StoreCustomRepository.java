package com.windmeal.store.repository;

import com.windmeal.store.dto.response.AllStoreResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface StoreCustomRepository {

  Slice<AllStoreResponse> getAllStoreInfo(Pageable pageable);
}
