package com.windmeal.member.service;


import com.windmeal.global.exception.ErrorCode;
import com.windmeal.global.util.AES256Util;
import com.windmeal.member.domain.Member;
import com.windmeal.member.domain.event.AlarmTestEvent;
import com.windmeal.member.dto.request.MemberAccountDeleteRequest;
import com.windmeal.member.dto.request.MemberInfoRequest;
import com.windmeal.member.dto.request.NicknameRequest;
import com.windmeal.member.dto.response.MemberInfoDTO;
import com.windmeal.member.dto.response.MyPageDTO;
import com.windmeal.member.exception.DuplicatedNicknameException;
import com.windmeal.member.exception.MemberNotFoundException;
import com.windmeal.member.exception.MemberNotMatchException;
import com.windmeal.member.repository.MemberRepository;
import com.windmeal.model.event.EventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

  private final MemberRepository memberRepository;
  private final AES256Util aes256Util;

  @Transactional
  public String registerNickname(NicknameRequest request, Long currentMemberId) {
    Member member = memberRepository.findById(currentMemberId)
        .orElseThrow(MemberNotFoundException::new);
    if (memberRepository.existsByNickname(request.getNickname())) {
      throw new DuplicatedNicknameException();
    }
    member.updateNickname(request.getNickname());
    return member.getNickname();
  }

  public boolean checkNickname(String nickname) {
    return memberRepository.existsByNickname(nickname);
  }

  @Transactional
  public MemberInfoDTO memberInfoDetails(MemberInfoRequest memberInfoRequest,
      Long currentMemberId) {
    String encryptToken = aes256Util.encrypt(memberInfoRequest.getAlarmToken());
    Member member = memberRepository.findById(currentMemberId)
        .orElseThrow(MemberNotFoundException::new);
    member.updateToken(encryptToken);
    return MemberInfoDTO.of(member.getId(), member.getEmail(), member.getNickname());
  }

  public MyPageDTO myInfoDetails(Long currentMemberId) {
    Member member = memberRepository.findById(currentMemberId)
        .orElseThrow(MemberNotFoundException::new);
    return MyPageDTO.of(member);
  }

  public void alarmTest(String msg, Long currentMemberId) {
    Member member = memberRepository.findById(currentMemberId)
        .orElseThrow(MemberNotFoundException::new);
    EventPublisher.publish(new AlarmTestEvent(msg, member.getToken()));
  }


  public void deleteAccount(MemberAccountDeleteRequest request, Long currentMemberId) {
    if(!currentMemberId.equals(request.getMemberId())) {
      throw new MemberNotMatchException();
    }
    Member member = memberRepository.findById(request.getMemberId()).orElseThrow(MemberNotFoundException::new);
    member.deleteAccount();
  }
}
