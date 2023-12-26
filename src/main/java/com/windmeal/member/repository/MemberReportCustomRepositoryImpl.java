package com.windmeal.member.repository;

import static com.windmeal.member.domain.QMember.member;
import static com.windmeal.member.domain.QMemberReport.memberReport;
import static com.windmeal.report.domain.QReport.report;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.windmeal.global.wrapper.RestSlice;
import com.windmeal.member.domain.QMemberReport;
import com.windmeal.member.dto.response.MemberReportListResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

@RequiredArgsConstructor
public class MemberReportCustomRepositoryImpl implements MemberReportCustomRepository{

  private final JPAQueryFactory jpaQueryFactory;
  @Override
  public Slice<MemberReportListResponse> getReportList(Pageable pageable, String nickName, String email,
      Boolean isReported) {
    List<MemberReportListResponse> content = jpaQueryFactory.select(
            Projections.constructor(MemberReportListResponse.class,
                memberReport.id,
                memberReport.reporter.id,
                memberReport.reporter.email,
                memberReport.reporter.nickname,
                memberReport.reported.id,
                memberReport.reported.email,
                memberReport.reported.nickname,
                memberReport.title,
                memberReport.content
            )
        )
        .from(memberReport)
        .join(memberReport.reported, member)
        .join(memberReport.reporter, member)
        .where(
            eqEmail(email, isReported),
            eqNickName(nickName, isReported)
        )
        .orderBy(memberReport.createdDate.desc())
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize() + 1).fetch();

    boolean hasNext = false;
    if (content.size() > pageable.getPageSize()) {
      content.remove(pageable.getPageSize());
      hasNext = true;
    }
    return new RestSlice(new SliceImpl(content, pageable, hasNext));
  }

  private BooleanExpression eqEmail(String email,Boolean isReported) {
    if(email==null)
      return null;

    if(isReported){
      memberReport.reported.email.contains(email);
    }

    return memberReport.reporter.email.contains(email);
  }

  private BooleanExpression eqNickName(String nickName,Boolean isReported) {
    if(nickName==null)
      return null;

    if(isReported){
      memberReport.reported.nickname.contains(nickName);
    }

    return memberReport.reporter.nickname.contains(nickName);
  }
}
