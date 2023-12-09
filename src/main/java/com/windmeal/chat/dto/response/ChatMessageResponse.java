package com.windmeal.chat.dto.response;

import com.windmeal.chat.domain.MessageDocument;
import com.windmeal.chat.domain.MessageType;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageResponse {

  private Page<ChatMessageSpecResponse> chatMessageSpecResponses;

  public static ChatMessageResponse of(Page<ChatMessageSpecResponse> chatMessageSpecResponses) {
    return ChatMessageResponse.builder()
        .chatMessageSpecResponses(chatMessageSpecResponses)
        .build();
  }


  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ChatMessageSpecResponse {
    private String message;
    private MessageType messageType;
    private Long senderId;
    private LocalDateTime createdTime;

    public static ChatMessageSpecResponse of(MessageDocument messageDocument) {
      return ChatMessageSpecResponse.builder()
          .messageType(messageDocument.getMessageType())
          .createdTime(messageDocument.getCreatedTime())
          .senderId(messageDocument.getSenderId())
          .message(messageDocument.getMessage())
          .build();
    }

  }
}
