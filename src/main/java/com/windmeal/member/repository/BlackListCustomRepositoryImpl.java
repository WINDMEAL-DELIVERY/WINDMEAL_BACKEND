package com.windmeal.member.repository;

import static com.windmeal.member.domain.QBlackList.blackList;
import static com.windmeal.member.domain.QMember.member;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.windmeal.member.dto.response.BlackListResponse;
import com.windmeal.order.dto.response.DeliveryListResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class BlackListCustomRepositoryImpl implements BlackListCustomRepository{

  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public Page<BlackListResponse> getBlackListByRequesterId(Pageable pageable, Long requesterId) {
    JPAQuery<BlackListResponse> query = jpaQueryFactory.select(
            Projections.constructor(BlackListResponse.class,
                blackList.id,
                blackList.blacked.email
            )
        )
        .from(blackList)
        .join(blackList.blacked, member)
        .where(blackList.requester.id.eq(requesterId));

    long size = query.fetchCount();
    List<BlackListResponse> result = query.fetch();
    return new PageImpl<>(result, pageable, size);
  }
}
