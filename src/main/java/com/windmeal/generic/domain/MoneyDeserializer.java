package com.windmeal.generic.domain;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;

public class MoneyDeserializer extends JsonDeserializer<Money> {

  @Override
  public Money deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
    int amount = p.getIntValue();
    return Money.wons(amount);
  }
}