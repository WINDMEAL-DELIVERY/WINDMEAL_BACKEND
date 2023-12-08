package com.windmeal.model.event;

import com.windmeal.order.domain.event.DeliveryCancelEvent;
import com.windmeal.order.domain.event.DeliveryMatchEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TestDeliveryCancelEventHandler {


  @Async
  @EventListener(DeliveryCancelEvent.class)
  public void handle(DeliveryCancelEvent event){
    System.out.println("DeliveryCancelEvent.getToken() = " + event.getToken());
  }

  @Async
  @EventListener(DeliveryMatchEvent.class)
  public void handle(DeliveryMatchEvent event){
    System.out.println("DeliveryMatchEvent.getToken() = " + event.getToken());
  }
}
