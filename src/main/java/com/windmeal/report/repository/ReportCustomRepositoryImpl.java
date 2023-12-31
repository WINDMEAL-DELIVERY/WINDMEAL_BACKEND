package com.windmeal.report.repository;

import static com.windmeal.member.domain.QMember.member;
import static com.windmeal.order.domain.QOrder.order;
import static com.windmeal.report.domain.QReport.report;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.windmeal.global.wrapper.RestSlice;
import com.windmeal.report.dto.response.ReportListResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

@RequiredArgsConstructor
public class ReportCustomRepositoryImpl implements ReportCustomRepository{

  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public Slice<ReportListResponse> getReportList(Pageable pageable, String nickName, String email) {
    List<ReportListResponse> content = jpaQueryFactory.select(
            Projections.constructor(ReportListResponse.class,
                report.id,
                member.id,
                member.email,
                member.nickname,
                report.title,
                report.content
            ))
        .from(report)
        .leftJoin(member).on(member.id.eq(report.reporter.id))
        .where(
            eqNickName(nickName),
            eqEmail(email)
        )
        .orderBy(report.createdDate.desc())
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize() + 1).fetch();

    boolean hasNext = false;
    if (content.size() > pageable.getPageSize()) {
      content.remove(pageable.getPageSize());
      hasNext = true;
    }
    return new RestSlice(new SliceImpl(content, pageable, hasNext));
  }

  private BooleanExpression eqEmail(String email) {
    if(email==null)
      return null;

    return member.email.contains(email);
  }

  private BooleanExpression eqNickName(String nickName) {
    if(nickName==null)
      return null;

    return member.nickname.contains(nickName);
  }
}
