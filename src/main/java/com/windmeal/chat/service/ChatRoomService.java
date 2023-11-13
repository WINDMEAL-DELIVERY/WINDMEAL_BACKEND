package com.windmeal.chat.service;

import com.windmeal.chat.domain.ChatRoom;
import com.windmeal.chat.dto.request.ChatRoomRequestDTO;
import com.windmeal.chat.dto.response.ChatRoomListResponse;
import com.windmeal.chat.dto.response.ChatRoomResponse;
import com.windmeal.chat.dto.response.ChatRoomSpecResponse;
import com.windmeal.chat.repository.ChatRoomRepository;
import com.windmeal.global.exception.ErrorCode;
import com.windmeal.member.domain.Member;
import com.windmeal.member.exception.MemberNotFoundException;
import com.windmeal.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;

    /**
     * 채팅방 생성을 관장하는 메서드
     * @param requestDTO
     * @return ChatRoomResponse
     */
    @Transactional
    public ChatRoomResponse createChatRoom(ChatRoomRequestDTO requestDTO) {
        Long ownerId = requestDTO.getOwnerId();
        Long guestId = requestDTO.getGuestId();
        Member owner = memberRepository.findById(ownerId)
                .orElseThrow(() -> new MemberNotFoundException(ErrorCode.NOT_FOUND, "해당 사용자를 찾을 수 없습니다."));
        Member guest = memberRepository.findById(guestId)
                .orElseThrow(() -> new MemberNotFoundException(ErrorCode.NOT_FOUND, "해당 사용자를 찾을 수 없습니다."));
        ChatRoom toEntity = requestDTO.toEntity(owner, guest);
        ChatRoom chatRoom = chatRoomRepository.save(toEntity);
        return ChatRoomResponse.of(chatRoom.getId(), chatRoom.getOwner().getId(), chatRoom.getGuest().getId());
    }

    /**
     * 채팅방 조회를 관장하는 메서드
     * 현재 사용자가 속한 모든 채팅방을 조회하고, 채팅방의 아이디와 속한 사용자들의 아이디를 리스트로 반환한다
     * @param memberId
     * @return ChatRoomListResponse
     */
    public ChatRoomListResponse getChatRoomsByMemberId(Long memberId) {
        // owner, guest 여부에 관계 없이 사용자가 참여한 채팅방 리스트를 반환받는다.
        // 사용자가 처음 접속하면 자신이 속한 채팅방을 모두 구독해야 하기 때문에, 채팅방의 아이디 리스트가 필요할 것이다.
        List<ChatRoom> chatRoomsByMemberIdIn = chatRoomRepository.findChatRoomsByMemberIdIn(memberId);
        // stream api는 속도 면에서 불리하지만, 사용자 한명의 채팅방은 그렇게까지 많지 않을 것이기 때문에 stream을 활용해서 타입을 바꿔주겠다.
        List<ChatRoomSpecResponse> list = chatRoomsByMemberIdIn.stream().map(ChatRoomSpecResponse::of).collect(Collectors.toList());
        return ChatRoomListResponse.of(list);
    }


}

