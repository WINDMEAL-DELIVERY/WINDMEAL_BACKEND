package com.windmeal.report.service;

import com.windmeal.member.domain.Member;
import com.windmeal.member.exception.MemberNotFoundException;
import com.windmeal.member.repository.MemberRepository;
import com.windmeal.report.domain.Report;
import com.windmeal.report.dto.request.ReportCreateRequest;
import com.windmeal.report.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportService {


  private final ReportRepository reportRepository;
  private final MemberRepository memberRepository;


  @Transactional
  public void createReport(ReportCreateRequest request){
    Member reporter = memberRepository.findById(request.getMemberId())
        .orElseThrow(MemberNotFoundException::new);

    reportRepository.save(Report.place(reporter,request));
  }

}
