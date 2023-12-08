package com.windmeal.model.event;

import lombok.Getter;

@Getter
public abstract class FCMEvent extends Event{

  private final String token;

  protected FCMEvent(String token) {
    this.token = token;
  }
}
