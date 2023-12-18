package com.windmeal.report.repository;

import com.windmeal.report.dto.response.ReportListResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReportCustomRepository {


  Page<ReportListResponse> getReportList(Pageable pageable, String nickName, String email);
}
