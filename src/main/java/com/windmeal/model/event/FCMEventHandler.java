package com.windmeal.model.event;

import com.windmeal.alarm.dto.FcmNotificationRequest;
import com.windmeal.alarm.service.FcmNotificationService;
import com.windmeal.global.util.AES256Util;
import com.windmeal.order.domain.event.DeliveryCancelEvent;
import com.windmeal.order.domain.event.DeliveryMatchEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FCMEventHandler {

  private final FcmNotificationService fcmNotificationService;

  private final AES256Util aes256Util;

  @Async
  @EventListener(DeliveryCancelEvent.class)
  public void handle(DeliveryCancelEvent event){
    FcmNotificationRequest message = FcmNotificationRequest.of(
        "배달 요청이 취소되었습니다.",
        String.format("%s 주문에 대한 배달 요청이 %s 한 이유로 취소되었습니다.", event.getSummary(),event.getContent()));
    fcmNotificationService.sendNotification(message,aes256Util.decrypt(event.getToken()));
  }

  @Async
  @EventListener(DeliveryMatchEvent.class)
  public void handle(DeliveryMatchEvent event){
    FcmNotificationRequest message = FcmNotificationRequest.of(
        "배달 주문이 성사되었습니다.",
        String.format("%s 주문에 대한 배달 요청이 성사되었습니다.", event.getSummary()));
    fcmNotificationService.sendNotification(message,aes256Util.decrypt(event.getToken()));
  }
}
