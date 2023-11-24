package com.windmeal.order.mapper;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Component;

@Component
public class StringToPointConverter implements Converter<String, Point> {
  @Override
  public Point convert(String source) {
    String[] parts = source.split(",");
    double latitude = Double.parseDouble(parts[0]);
    double longitude = Double.parseDouble(parts[1]);
    return new Point(latitude, longitude);
  }
}

