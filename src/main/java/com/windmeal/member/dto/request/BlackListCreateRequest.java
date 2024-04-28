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
@Schema(title = "사용자 차단 요청")
public class BlackListCreateRequest {

  @Schema(description = "차단할 사용자의 Id", example = "1")
  private Long id;
}
