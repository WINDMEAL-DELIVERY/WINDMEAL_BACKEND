package com.windmeal.member.service;


import com.windmeal.member.domain.Member;
import com.windmeal.member.domain.MemberReport;
import com.windmeal.member.dto.request.MemberReportCreateRequest;
import com.windmeal.member.dto.response.MemberReportListResponse;
import com.windmeal.member.exception.MemberNotFoundException;
import com.windmeal.member.repository.MemberReportRepository;
import com.windmeal.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberReportService {

  /**
   * 멤버 신고
   * 멤버 신고 조회
   */

  private final MemberRepository memberRepository;

  private final MemberReportRepository memberReportRepository;
  @Transactional
  public void createMemberReport(Long currentMemberId, MemberReportCreateRequest request){
    Member reporter = memberRepository.findById(currentMemberId)
        .orElseThrow(MemberNotFoundException::new);

    Member reported = memberRepository.findById(request.getReportedId())
        .orElseThrow(MemberNotFoundException::new);

    memberReportRepository.save(MemberReport.place(request,reported,reporter));
  }

  public Slice<MemberReportListResponse> getReportList(Pageable pageable, String nickName, String email,
      Boolean isReported) {

    return memberReportRepository.getReportList(pageable,nickName,email,isReported);
  }
}
