package com.windmeal.report.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "건의사항", description = "건의사항 관련 api 입니다.")
@RestController
@RequiredArgsConstructor
public class ReportController {

  /**
   * 1. 건의 사항 생성
   * - 생성 시 관리자에게 알림 or 이메일 전송
   * 2. 건의 사항 조회
   */
}

