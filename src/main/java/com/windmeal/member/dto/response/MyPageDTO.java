package com.windmeal.member.dto.response;

import com.windmeal.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class MyPageDTO {
    private Long id;
    private String name;
    private String email;
    private String nickname;
    private String department;

    public static MyPageDTO of(Member member) {
        return MyPageDTO.builder()
            .id(member.getId())
            .name(member.getName())
            .email(member.getEmail())
            .nickname(member.getNickname())
            .department(member.getDepartment())
            .build();
    }
}
