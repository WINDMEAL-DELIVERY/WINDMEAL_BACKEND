package com.windmeal.order.domain.event;

import com.windmeal.model.event.FCMEvent;
import lombok.Getter;

@Getter
public class DeliveryCancelEvent extends FCMEvent {

  private final String content;

  private final String summary;
  public DeliveryCancelEvent(String token,String content, String summary) {

    super(token);
    this.content = content;
    this.summary = summary;
  }
}
