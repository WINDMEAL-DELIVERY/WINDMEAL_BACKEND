package com.windmeal.chat.domain;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Document(collection = "chatroom")
public class ChatroomDocument {

  @Id
  private String id;

  private Long ownerId;

  private Long guestId;

  private Long orderId;

  private String ownerEmail;

  private String guestEmail;

  private boolean isDeletedByOwner = false;

  private boolean isDeletedByGuest = false;

  private String ownerAlarmToken;

  private String guestAlarmToken;


  @CreatedDate
  private LocalDateTime createdTime;

  @Builder
  public ChatroomDocument(Long ownerId, Long guestId, Long orderId, String ownerEmail,
      String guestEmail, String ownerAlarmToken, String guestAlarmToken) {
    this.ownerId = ownerId;
    this.guestId = guestId;
    this.orderId = orderId;
    this.ownerEmail = ownerEmail;
    this.guestEmail = guestEmail;
    this.isDeletedByOwner = false;
    this.isDeletedByGuest = false;
    this.ownerAlarmToken = ownerAlarmToken;
    this.guestAlarmToken = guestAlarmToken;
  }


}
