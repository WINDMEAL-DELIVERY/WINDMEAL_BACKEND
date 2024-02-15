package com.windmeal.global.util;

import java.time.*;

public class TimeUtil {

    private static final ZoneId KOREA_ZONE_ID = ZoneId.of("Asia/Seoul");

    public static LocalTime convertToKoreanTime(LocalTime time) {
        return time.atDate(LocalDate.now())
            .atZone(ZoneId.systemDefault())
            .withZoneSameInstant(KOREA_ZONE_ID)
            .toLocalTime();
    }
}
