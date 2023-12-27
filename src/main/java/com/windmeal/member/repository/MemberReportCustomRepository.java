package com.windmeal.member.repository;

import com.windmeal.member.dto.response.MemberReportListResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface MemberReportCustomRepository {

  Slice<MemberReportListResponse> getReportList(Pageable pageable, String nickName, String email,
      Boolean isReported);
}
