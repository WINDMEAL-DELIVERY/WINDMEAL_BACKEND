package com.windmeal.member.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.windmeal.annotation.IntegrationTest;
import com.windmeal.member.domain.Member;
import com.windmeal.member.dto.request.MemberInfoRequest;
import com.windmeal.member.dto.request.NicknameRequest;
import com.windmeal.member.dto.response.MemberInfoDTO;
import com.windmeal.member.exception.DuplicatedNicknameException;
import com.windmeal.member.exception.MemberNotFoundException;
import com.windmeal.member.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
public class MemberServiceTest {

  @Autowired
  private MemberService memberService;
  @Autowired
  private MemberRepository memberRepository;

  @AfterEach
  void tearDown() {
    memberRepository.deleteAllInBatch();
  }

  // 사용자 생성 요청
  private Member createMember(String name, String nickname, String department) {
    return Member.builder()
        .department(department)
        .nickname(nickname)
        .name(name)
        .build();
  }

  private NicknameRequest createNicknameRequest(String nickname) {
    return NicknameRequest.of(nickname);
  }

  private MemberInfoRequest createMemberInfoRequest(String alarmToken) {
    return MemberInfoRequest.of(alarmToken);
  }

  @Test
  @DisplayName("닉네임 등록 - 성공")
  public void registerNickname() {
    //given
    Member preMember = createMember("최배달", null, "배달학과");
    Member member = memberRepository.save(preMember);

    //when
    memberService.registerNickname(createNicknameRequest("뚜벅이"), member.getId());
    Member memberWalker = memberRepository.findById(member.getId()).get();

    //then
    assertThat(memberWalker.getNickname()).isEqualTo("뚜벅이");
  }

  @Test
  @DisplayName("닉네임 등록 - 실패 : 존재하지 않는 사용자")
  public void registerNicknameMemberNotFound() {
    // given
    NicknameRequest nicknameRequest = createNicknameRequest("뚜벅이");
    // when & then
    Assertions.assertThrows(MemberNotFoundException.class,
        () -> memberService.registerNickname(nicknameRequest, 1L));
    assertThatThrownBy(() -> memberService.registerNickname(nicknameRequest, 1L))
        .isInstanceOf(MemberNotFoundException.class)
        .hasMessage("존재하지 않는 사용자입니다.");
  }

  @Test
  @DisplayName("닉네임 등록 - 실패 : 중복된 닉네임")
  public void registerNicknameWithEmptyNickname() {
    //given
    Member preMember = createMember("최배달", null, "배달학과");
    Member anotherMember = createMember("김배달", "뚜벅이", "배달학과");
    memberRepository.save(anotherMember);

    // when
    NicknameRequest nicknameRequest = createNicknameRequest("뚜벅이");
    Member member = memberRepository.save(preMember);

    //then
    assertThatThrownBy(() -> memberService.registerNickname(nicknameRequest, member.getId()))
        .isInstanceOf(DuplicatedNicknameException.class)
        .hasMessage("이미 사용 중인 닉네임입니다.");
  }

  @Test
  @DisplayName("닉네임 중복 확인 - 성공 : 중복이 있는 경우")
  public void duplicatedNicknameCheck() {
    //given
    Member anotherMember = createMember("김배달", "뚜벅이", "배달학과");
    memberRepository.save(anotherMember);

    //when
    boolean result = memberService.checkNickname("뚜벅이");

    //then
    assertThat(result).isTrue();
  }

  @Test
  @DisplayName("닉네임 중복 확인 - 성공 : 중복이 없는 경우")
  public void notDuplicatedNicknameCheck() {
    //given
    Member anotherMember = createMember("김배달", "뚜벅초", "배달학과");
    memberRepository.save(anotherMember);

    //when
    boolean result = memberService.checkNickname("뚜벅이");

    //then
    assertThat(result).isFalse();
  }

  @Test
  @DisplayName("사용자 정보 토큰 교환 - 성공")
  public void exchangeTokenWithMemberInfo() {
    //given
    Member preMember = createMember("최배달", null, "배달학과");
    Member member = memberRepository.save(preMember);
    String tempAlarmToken = "tempAlarmToken";

    //when
    MemberInfoRequest memberInfoRequest = createMemberInfoRequest(tempAlarmToken);
    MemberInfoDTO memberInfoDTO = memberService.memberInfoDetails(memberInfoRequest,
        member.getId());

    //then
    assertThat(memberInfoDTO).extracting("id", "nickname")
        .containsExactly(member.getId(), member.getNickname());
  }

  @Test
  @DisplayName("사용자 정보 토큰 교환 - 실패 : 존재하지 않는 사용자")
  public void exchangeTokenWithMemberInfoFail() {
    //given
    Member preMember = createMember("최배달", null, "배달학과");
    Member member = memberRepository.save(preMember);
    String tempAlarmToken = "tempAlarmToken";

    //when
    MemberInfoRequest memberInfoRequest = createMemberInfoRequest(tempAlarmToken);

    //then
    assertThatThrownBy(() -> memberService.memberInfoDetails(memberInfoRequest, 2L))
        .isInstanceOf(MemberNotFoundException.class)
        .hasMessage("존재하지 않는 사용자입니다.");
  }

  @Test
  @DisplayName("사용자 정보 반환 - 성공")
  public void memberDetails() {
    //given
    Member preMember = createMember("최배달", null, "배달학과");
    Member member = memberRepository.save(preMember);

    //when
    MemberInfoDTO memberInfoDTO = memberService.myInfoDetails(member.getId());

    //then
    assertThat(memberInfoDTO).extracting("id", "nickname")
        .containsExactly(member.getId(), member.getNickname());
  }

  @Test
  @DisplayName("사용자 정보 반환 - 실패 : 존재하지 않는 사용자")
  public void emptyMemberDetails() {
    //given
    Member preMember = createMember("최배달", null, "배달학과");
    Member member = memberRepository.save(preMember);

    //when & then
    assertThatThrownBy(() -> memberService.myInfoDetails(2L))
        .isInstanceOf(MemberNotFoundException.class)
        .hasMessage("존재하지 않는 사용자입니다.");
  }

}
