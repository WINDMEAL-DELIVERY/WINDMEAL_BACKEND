package com.windmeal.report.domain.event;

import com.windmeal.model.event.Event;
import lombok.Getter;

@Getter
public class ReportEvent extends Event {


  private final String title;

  private final String nickName;

  public ReportEvent(String title, String nickName) {
    this.title = title;
    this.nickName = nickName;
  }
}
