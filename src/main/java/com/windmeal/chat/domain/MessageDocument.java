package com.windmeal.chat.domain;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@NoArgsConstructor
@Document(collection = "chat")
public class MessageDocument {

  @Id
  private String id;

  @Indexed
  private Long chatroomId;

  private MessageType messageType;

  private String message;

  private Long senderId;
  private String senderEmail;

  @CreatedDate
  private LocalDateTime createdTime;


}
