package com.windmeal.member.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import static com.windmeal.global.constants.PatternConstants.NICKNAME_PATTERN;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NicknameRequest {

    @NotBlank(message = "닉네임은 빈칸이 될 수 없습니다.")
    @Pattern(regexp = NICKNAME_PATTERN, message = "닉네임에는 특수문자가 포함될 수 없습니다.")
    private String nickname;

    public static NicknameRequest of(String nickname) {
        return NicknameRequest.builder()
                .nickname(nickname)
                .build();
    }
}
