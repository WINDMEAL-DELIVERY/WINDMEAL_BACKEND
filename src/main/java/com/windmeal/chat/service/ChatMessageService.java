package com.windmeal.chat.service;

import com.windmeal.chat.domain.MessageDocument;
import com.windmeal.chat.dto.response.ChatMessageResponse;
import com.windmeal.chat.dto.response.ChatMessageResponse.ChatMessageSpecResponse;
import com.windmeal.chat.repository.MessageDocumentRepository;
import com.windmeal.global.exception.ErrorCode;
import com.windmeal.member.exception.MemberNotFoundException;
import com.windmeal.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatMessageService {

  private final MemberRepository memberRepository;
  private final MessageDocumentRepository messageDocumentRepository;

  public ChatMessageResponse findChatMessages(Long chatroomId, Long currentMemberId,
      Pageable pageable) {
    if (!memberRepository.existsById(currentMemberId)) {
      throw new MemberNotFoundException(ErrorCode.NOT_FOUND, "사용자를 찾을 수 없습니다.");
    }
    Slice<MessageDocument> messageDocuments = messageDocumentRepository.findByChatroomId(chatroomId,
        pageable);
    Slice<ChatMessageSpecResponse> chatMessageSpecResponses = messageDocuments.map(
        ChatMessageSpecResponse::of);
    return ChatMessageResponse.of(chatMessageSpecResponses);
  }
}
