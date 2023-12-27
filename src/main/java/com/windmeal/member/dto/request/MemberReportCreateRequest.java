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

  @Schema(description = "신고하는 유저 ID", example = "1")
  private Long reporterId;

  @Schema(description = "신고 유저 ID", example = "1")
  private Long reportedId;

  @Schema(description = "신고 제목", example = "신고 제목")
  private String title;
  @Schema(description = "신고 내용", example = "신고 내용")
  private String content;


  public MemberReportCreateRequest toServiceDto(Long reporterId){
    this.setReporterId(reporterId);
    return this;
  }
  private void setReporterId(Long reporterId) {
    this.reporterId = reporterId;
  }
}
