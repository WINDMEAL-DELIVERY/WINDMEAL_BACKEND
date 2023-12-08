package com.windmeal.order.domain.event;

import com.windmeal.model.event.Event;
import com.windmeal.model.event.FCMEvent;
import lombok.Getter;

@Getter
public class DeliveryCancelEvent extends FCMEvent {


  public DeliveryCancelEvent(String token) {
    super(token);
  }
}
