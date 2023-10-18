package com.windmeal.store.dto.response;

import com.windmeal.store.domain.Store;
import com.windmeal.store.validator.StoreValidator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.geo.Point;

import java.time.LocalTime;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class StoreResponse {
    private Long storeId;
    private Long ownerId;

    private String name;

    private String phoneNumber;

    private String photo;
    private LocalTime openTime;

    private LocalTime closeTime;

    private Point location;

    private boolean isOpen;

    public static StoreResponse of(Store store, StoreValidator storeValidator) {
        return StoreResponse.builder()
                .storeId(store.getId())
                .ownerId(store.getOwner().getId())
                .name(store.getName())
                .phoneNumber(store.getPhoneNumber())
                .photo(store.getPhoto())
                .openTime(store.getOpenTime())
                .closeTime(store.getCloseTime())
                .location(store.getLocation())
                .isOpen(storeValidator.validateStoreIsOpen(store.getOpenTime(),store.getCloseTime(),LocalTime.now()))
                .build();
    }
}
