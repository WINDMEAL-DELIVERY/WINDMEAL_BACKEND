package com.windmeal.store.validator;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StoreValidatorTest {

    @Autowired
    private StoreValidator storeValidator;

    @DisplayName("현재 시간이 가게가 운영하는 시간이면 true 를 반환하고 운영 시간이 아닌 경우 false 를 반환한다.")
    @Test
    void validateStoreIsOpen(){
        //given
        LocalTime openTime = LocalTime.of(10, 0, 0);
        LocalTime closeTime = LocalTime.of(20, 0, 0);

        //when
        LocalTime openTimeBefore1 = LocalTime.of(9, 59, 59);
        LocalTime openTimeExactly = LocalTime.of(10, 0, 0);
        LocalTime openTimeAfter1 = LocalTime.of(10, 0, 1);
        LocalTime closeTimeExactly = LocalTime.of(20, 0, 0);
        LocalTime closeTimeAfter1 = LocalTime.of(20, 0, 1);

        //then
        assertThat(storeValidator.validateStoreIsOpen(openTime,closeTime,openTimeBefore1)).isFalse();
        assertThat(storeValidator.validateStoreIsOpen(openTime,closeTime,openTimeExactly)).isTrue();
        assertThat(storeValidator.validateStoreIsOpen(openTime,closeTime,openTimeAfter1)).isTrue();
        assertThat(storeValidator.validateStoreIsOpen(openTime,closeTime,closeTimeExactly)).isTrue();
        assertThat(storeValidator.validateStoreIsOpen(openTime,closeTime,closeTimeAfter1)).isFalse();

    }
}