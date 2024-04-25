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

  @Schema(description = "신고 내용", example = "신고 내용")
  private String content;

}
