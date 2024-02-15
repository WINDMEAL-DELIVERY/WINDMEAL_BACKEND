package com.windmeal.store.repository;

import com.windmeal.order.domain.OrderStatus;
import com.windmeal.order.dto.response.OrderMapListResponse;
import com.windmeal.store.dto.response.AllStoreResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface StoreCustomRepository {

    Slice<AllStoreResponse> getAllStoreInfo(Pageable pageable);

    List<OrderMapListResponse> getStoreMapList(Long storeId, String eta,
                                               String storeCategory, Long placeId, OrderStatus orderStatus);
}
