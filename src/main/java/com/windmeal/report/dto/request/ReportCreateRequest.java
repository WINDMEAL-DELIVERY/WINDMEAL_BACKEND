package com.windmeal.report.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "건의사항")
public class ReportCreateRequest {

  @Schema(description = "건의 요청자 ID", example = "1")
  private Long memberId;
  @Schema(description = "신고 제목", example = "신고 제목")
  private String title;
  @Schema(description = "신고 내용", example = "신고 내용")
  private String content;

  public ReportCreateRequest toServiceDto(Long memberId){
    setMemberId(memberId);
    return this;
  }

  private void setMemberId(Long memberId) {
    this.memberId = memberId;
  }
}
