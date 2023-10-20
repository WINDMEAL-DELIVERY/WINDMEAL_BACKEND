package com.windmeal.store.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StoreUpdateRequest {

    @NotBlank(message = "가게 이름은 빈칸일 수 없습니다.")
    private String name;

    private String phoneNumber;

    private LocalTime openTime;

    private LocalTime closeTime;

    private Double longitude;

    private Double latitude;
}
