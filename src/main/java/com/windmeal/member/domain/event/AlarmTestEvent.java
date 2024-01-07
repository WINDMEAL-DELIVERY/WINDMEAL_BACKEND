package com.windmeal.member.domain.event;

import com.windmeal.model.event.FCMEvent;
import lombok.Getter;

@Getter
public class AlarmTestEvent extends FCMEvent {

  private final String msg;

  public AlarmTestEvent(String msg, String token) {
    super(token);
    this.msg = msg;
  }


}
