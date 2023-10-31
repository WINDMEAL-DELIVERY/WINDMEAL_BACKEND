package com.windmeal.chat.service;

import com.windmeal.chat.domain.ChatRoom;
import com.windmeal.chat.dto.request.ChatRoomRequestDTO;
import com.windmeal.chat.dto.response.ChatRoomResponseDTO;
import com.windmeal.chat.repository.ChatRoomRepository;
import com.windmeal.global.exception.ErrorCode;
import com.windmeal.member.domain.Member;
import com.windmeal.member.exception.MemberNotFoundException;
import com.windmeal.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomService {
    private ChatRoomRepository chatRoomRepository;
    private MemberRepository memberRepository;

    @Transactional
    public ChatRoomResponseDTO createChatRoom(ChatRoomRequestDTO requestDTO) {
        Long ownerId = requestDTO.getOwnerId();
        Long guestId = requestDTO.getGuestId();
        Member owner = memberRepository.findById(ownerId)
                .orElseThrow(() -> new MemberNotFoundException(ErrorCode.NOT_FOUND, "해당 사용자를 찾을 수 없습니다."));
        Member guest = memberRepository.findById(guestId)
                .orElseThrow(() -> new MemberNotFoundException(ErrorCode.NOT_FOUND, "해당 사용자를 찾을 수 없습니다."));
        ChatRoom toEntity = requestDTO.toEntity(owner, guest);
        ChatRoom chatRoom = chatRoomRepository.save(toEntity);
        return ChatRoomResponseDTO.of(chatRoom.getId(), chatRoom.getOwner().getId(), chatRoom.getGuest().getId());
    }

//    public ChatRoomResponseDTO getChatRoomByMemberId(Long memberId) {
//        // owner, guest 여부에 관계 없이 사용자가 참여한 채팅방 리스트를 반환받는다.
//        chatRoomRepository.
//    }
}

