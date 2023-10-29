package com.windmeal.chat.dto.response;

import com.windmeal.chat.domain.MessageType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class ChatResponseDTO {
    private MessageType messageType;
    private Long roomId;
    private Long sender;
    private String message;
    private LocalDateTime time;

    public static ChatResponseDTO of(MessageType type, Long roomId, Long sender, String message, LocalDateTime time) {
        return ChatResponseDTO.builder()
                .messageType(type)
                .roomId(roomId)
                .sender(sender)
                .message(message)
                .time(time)
                .build();
    }
}
