package com.windmeal.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberReportCreateRequest {

  @Schema(description = "신고 유저 ID", example = "1")
  private Long reportedId;

  @Schema(description = "신고 내용", example = "신의한컵 30인분 25만원 부탁했는데 먹튀했어요")
  private String content;

  @Schema(description = "신고 제목", example = "먹튀범 \"나는 동혁\" 신고합니다.")
  private String title;
}
