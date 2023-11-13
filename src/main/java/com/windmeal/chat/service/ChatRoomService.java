package com.windmeal.chat.service;

import com.windmeal.chat.domain.ChatRoom;
import com.windmeal.chat.dto.request.ChatRoomDeleteRequest;
import com.windmeal.chat.dto.request.ChatRoomRequest;
import com.windmeal.chat.dto.response.ChatRoomListResponse;
import com.windmeal.chat.dto.response.ChatRoomResponse;
import com.windmeal.chat.dto.response.ChatRoomSpecResponse;
import com.windmeal.chat.exception.ChatRoomNotFoundException;
import com.windmeal.chat.exception.InvalidRequesterException;
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
    public ChatRoomResponse createChatRoom(ChatRoomRequest requestDTO, Long currentMemberId) {
        Long ownerId = requestDTO.getOwnerId();
        if(currentMemberId.equals(ownerId)) {
            throw new InvalidRequesterException(ErrorCode.VALIDATION_ERROR, "요청자와 접속자의 정보가 다릅니다.");
        }
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

    /**
     * 채팅방 삭제를 관장하는 메서드
     * @param deleteRequest
     * @param currentMemberId
     */
    @Transactional
    public void deleteChatRoom(ChatRoomDeleteRequest deleteRequest, Long currentMemberId) {
//        ChatRoom chatRoom = chatRoomRepository.findByIdWithFetchJoin(deleteRequest.getRoomId())
//                .orElseThrow(() -> new ChatRoomNotFoundException(ErrorCode.NOT_FOUND, "채팅방이 존재하지 않습니다."));
        // 요청으로 들어온 채팅방에 사용자가 속한지 확인해야 한다.
        ChatRoom chatRoom = chatRoomRepository.findByIdWithFetchJoin(deleteRequest.getRoomId());
        if(chatRoom == null) {
            throw new ChatRoomNotFoundException(ErrorCode.NOT_FOUND, "채팅방이 존재하지 않습니다.");
        }
        if(chatRoom.getOwner().getId() != currentMemberId && chatRoom.getGuest().getId() !=  currentMemberId) {
            throw new InvalidRequesterException(ErrorCode.VALIDATION_ERROR, "요청자가 해당 채팅방에 속해있지 않습니다.");
        }
        chatRoomRepository.delete(chatRoom);
    }

}

