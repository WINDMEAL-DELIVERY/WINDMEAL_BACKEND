package com.windmeal.report.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "건의사항 조회")
public class ReportListResponse {

  @Schema(description = "건의사항 ID", example = "1")
  private Long reportId;

  @Schema(description = "건의 요청자 ID", example = "1")
  private Long memberId;

  @Schema(description = "건의 요청자 email", example = "idh1007@naver.com")
  private String email;

  @Schema(description = "건의 요청자 nickname", example = "임동동")
  private String nickName;

  @Schema(description = "신고 제목", example = "신고 제목")
  private String title;
  @Schema(description = "신고 내용", example = "신고 내용")
  private String content;
}
