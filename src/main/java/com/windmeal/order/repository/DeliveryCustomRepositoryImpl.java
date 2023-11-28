package com.windmeal.order.repository;

import static com.windmeal.member.domain.QMember.member;
import static com.windmeal.model.place.QPlace.place;
import static com.windmeal.order.domain.QDelivery.delivery;
import static com.windmeal.order.domain.QOrder.order;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.windmeal.order.domain.DeliveryStatus;
import com.windmeal.order.dto.response.DeliveryListResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class DeliveryCustomRepositoryImpl implements DeliveryCustomRepository {

  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public Page<DeliveryListResponse> getOwnDelivering(Long memberId, LocalDate today,
      Pageable pageable) {
    JPAQuery<DeliveryListResponse> query = jpaQueryFactory.select(
            Projections.constructor(DeliveryListResponse.class,
                delivery.id,
                order.id,
                delivery.deliveryStatus,
                order.summary,
                order.description,
                place.name,
                jpaQueryFactory.select(member.nickname).from(member)
                    .where(member.id.eq(order.orderer_id))
            ))
        .from(delivery)
        .join(delivery.order, order)
        .join(order.place, place)
        .where(
            delivery.deliver.id.eq(memberId),
            delivery.deliveryStatus.eq(DeliveryStatus.DELIVERING),
            eqEta(today)
        );
    long size = query.fetchCount();
    List<DeliveryListResponse> result = query.fetch();
    return new PageImpl<>(result, pageable, size);
  }

  @Override
  public Page<DeliveryListResponse> getOwnOrdering(Long memberId, LocalDate today,
      Pageable pageable) {
    JPAQuery<DeliveryListResponse> query = jpaQueryFactory.select(
            Projections.constructor(DeliveryListResponse.class,
                delivery.id,
                order.id,
                delivery.deliveryStatus,
                order.summary,
                order.description,
                place.name,
                jpaQueryFactory.select(member.nickname).from(member)
                    .where(member.id.eq(delivery.deliver.id))
            ))
        .from(delivery)
        .join(delivery.order, order)
        .join(order.place, place)
        .where(
            order.orderer_id.eq(memberId),
            delivery.deliveryStatus.eq(DeliveryStatus.DELIVERING),
            eqEta(today)
        );
    long size = query.fetchCount();
    List<DeliveryListResponse> result = query.fetch();
    return new PageImpl<>(result, pageable, size);
  }

  private BooleanExpression eqEta(LocalDate now) {
    LocalTime start = LocalTime.MIN;
    LocalTime end = LocalTime.MAX;
    return delivery.order.eta.between(now.atTime(start), now.atTime(end));
  }
}
