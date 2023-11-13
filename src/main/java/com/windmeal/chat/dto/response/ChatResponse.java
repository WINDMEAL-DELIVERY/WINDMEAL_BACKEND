package com.windmeal.chat.dto.response;

import com.windmeal.chat.domain.MessageType;
import lombok.*;

import java.time.LocalDateTime;

// TODO 삭제 예정 클래스.
@Getter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class ChatResponse {
    private MessageType messageType;
    private Long roomId;
    private Long sender;
    private String message;
    private LocalDateTime time;

    public static ChatResponse of(MessageType type, Long roomId, Long sender, String message, LocalDateTime time) {
        return ChatResponse.builder()
                .messageType(type)
                .roomId(roomId)
                .sender(sender)
                .message(message)
                .time(time)
                .build();
    }
}
