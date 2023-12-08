package com.windmeal.member.service;


import com.windmeal.global.exception.ErrorCode;
import com.windmeal.global.util.AES256Util;
import com.windmeal.member.domain.Member;
import com.windmeal.member.dto.request.MemberInfoRequest;
import com.windmeal.member.dto.request.NicknameRequest;
import com.windmeal.member.dto.response.MemberInfoDTO;
import com.windmeal.member.exception.DuplicatedNicknameException;
import com.windmeal.member.exception.MemberNotFoundException;
import com.windmeal.member.repository.MemberRepository;
import java.util.List;
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
        .orElseThrow(() -> new MemberNotFoundException(ErrorCode.NOT_FOUND, "사용자를 찾을 수 없습니다."));
    if (memberRepository.existsByNickname(request.getNickname())) {
      throw new DuplicatedNicknameException(ErrorCode.VALIDATION_ERROR, "이미 사용 중인 닉네임입니다.");
    }
    member.updateNickname(request.getNickname());
    return member.getNickname();
  }

  public boolean checkNickname(String nickname) {
    return memberRepository.existsByNickname(nickname);
  }

  @Transactional
  public MemberInfoDTO findMemberInfo(MemberInfoRequest memberInfoRequest, Long currentMemberId) {
    // TODO 토큰 암호화 후 저장
    String decrypt = aes256Util.encrypt(memberInfoRequest.getAlarmToken());
    Member member = memberRepository.findById(currentMemberId)
        .orElseThrow(() -> new MemberNotFoundException(ErrorCode.NOT_FOUND, "사용자를 찾을 수 없습니다."));
    return MemberInfoDTO.of(member.getId(), member.getEmail(), member.getNickname());
  }

}
