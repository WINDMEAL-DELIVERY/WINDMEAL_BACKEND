package com.windmeal.chat.dto.response;

import com.windmeal.chat.domain.MessageDocument;
import com.windmeal.chat.domain.MessageType;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageDocumentResponse {

  private LocalDateTime createdTime;
  private MessageType messageType;
  private String senderEmail;
  private Long chatroomId;
  private String message;
  private Long senderId;

  public static MessageDocumentResponse of(MessageDocument messageDocument) {
    return MessageDocumentResponse.builder()
        .messageType(messageDocument.getMessageType())
        .senderEmail(messageDocument.getSenderEmail())
        .createdTime(messageDocument.getCreatedTime())
        .chatroomId(messageDocument.getChatroomId())
        .senderId(messageDocument.getSenderId())
        .message(messageDocument.getMessage())
        .build();
  }
}
