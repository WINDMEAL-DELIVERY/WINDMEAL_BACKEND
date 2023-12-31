package com.windmeal.order.domain.event;

import com.windmeal.model.event.FCMEvent;
import lombok.Getter;

@Getter
public class DeliveryMatchEvent extends FCMEvent {

  private final String summary;
  public DeliveryMatchEvent(String summary,String token) {
    super(token);
    this.summary = summary;
  }

}
