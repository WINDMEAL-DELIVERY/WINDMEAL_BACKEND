package com.windmeal.member.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class MemberReportListResponse {

  @Schema(description = "멤버 신고 ID", example = "1")
  private Long memberReportId;

  @Schema(description = "신고 요청자 ID", example = "1")
  private Long reporterId;

  @Schema(description = "신고 요청자 email", example = "idh1007@naver.com")
  private String reporterEmail;

  @Schema(description = "신고 요청자 nickname", example = "임동동")
  private String reporterNickName;

  @Schema(description = "피신고인 ID", example = "1")
  private Long reportedId;

  @Schema(description = "피신고인자 email", example = "idh1007@naver.com")
  private String reportedEmail;

  @Schema(description = "피신고인 nickname", example = "임동동")
  private String reportedNickName;

  @Schema(description = "신고 제목", example = "신고 제목")
  private String title;
  @Schema(description = "신고 내용", example = "신고 내용")
  private String content;
}
