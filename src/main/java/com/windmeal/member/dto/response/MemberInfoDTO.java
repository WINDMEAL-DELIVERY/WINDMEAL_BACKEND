package com.windmeal.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Getter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class MemberInfoDTO {

  private Long id;
  private String email;
  private String nickname;

  public static MemberInfoDTO of(Long memberId, String email, String nickname) {
    return MemberInfoDTO.builder()
        .id(memberId)
        .email(email)
        .nickname(nickname)
        .build();
  }

}
