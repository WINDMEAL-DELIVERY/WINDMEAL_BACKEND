package com.windmeal.member.dto.request;


import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberInfoRequest {

  @NotBlank(message = "토큰값은 반드시 존재해야 합니다.")
  private String alarmToken;
  public static MemberInfoRequest of(String alarmToken) {
    return new MemberInfoRequest(alarmToken);
  }

}
