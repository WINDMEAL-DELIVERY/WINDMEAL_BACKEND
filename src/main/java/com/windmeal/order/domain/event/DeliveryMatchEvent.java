package com.windmeal.order.domain.event;

import com.windmeal.model.event.Event;
import com.windmeal.model.event.FCMEvent;
import lombok.Getter;

@Getter
public class DeliveryMatchEvent extends FCMEvent {

  public DeliveryMatchEvent(String token) {
    super(token);
  }

//  private final
}
