package com.windmeal.chat.service;

import com.querydsl.core.types.Predicate;
import com.windmeal.chat.domain.MessageDocument;
import com.windmeal.chat.domain.QMessageDocument;
import com.windmeal.chat.dto.response.ChatMessageResponse;
import com.windmeal.chat.dto.response.ChatMessageResponse.ChatMessageSpecResponse;
import com.windmeal.chat.repository.MessageDocumentRepository;
import com.windmeal.global.exception.ErrorCode;
import com.windmeal.member.exception.MemberNotFoundException;
import com.windmeal.member.repository.MemberRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    QMessageDocument qMessageDocument = QMessageDocument.messageDocument;
    Predicate predicate = qMessageDocument.chatroomId.eq(chatroomId);
    Page<MessageDocument> messageDocuments = messageDocumentRepository.findAll(predicate, pageable);
    Page<ChatMessageSpecResponse> chatMessageSpecResponses = messageDocuments.map(
        ChatMessageSpecResponse::of);
    return ChatMessageResponse.of(chatMessageSpecResponses);
  }
}
