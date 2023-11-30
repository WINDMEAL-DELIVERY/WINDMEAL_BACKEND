package com.windmeal.generic.domain;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import org.springframework.boot.jackson.JsonComponent;

@JsonComponent
public class MoneyDeserializer extends JsonDeserializer<Money> {

  @Override
  public Money deserialize(JsonParser price, DeserializationContext ctxt) throws IOException, JsonProcessingException {
    int amount = price.getValueAsInt();
    return Money.wons(amount);
  }
}