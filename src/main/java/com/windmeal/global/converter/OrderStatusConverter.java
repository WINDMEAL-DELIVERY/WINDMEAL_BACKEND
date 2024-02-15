package com.windmeal.global.converter;

import com.windmeal.order.domain.OrderStatus;
import org.springframework.core.convert.converter.Converter;

public class OrderStatusConverter implements Converter<String, OrderStatus> {

    @Override
    public OrderStatus convert(String source) {
        return OrderStatus.of(source);
    }
}
