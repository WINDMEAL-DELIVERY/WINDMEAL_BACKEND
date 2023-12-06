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
import com.windmeal.order.domain.OrderStatus;
import com.windmeal.order.dto.response.DeliveryListResponse;
import com.windmeal.order.dto.response.OrderingListResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

@RequiredArgsConstructor
public class DeliveryCustomRepositoryImpl implements DeliveryCustomRepository {

  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public Slice<DeliveryListResponse> getOwnDelivering(Long memberId, LocalDate today,
      Pageable pageable) {
    List<DeliveryListResponse> content = jpaQueryFactory.select(
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
        )
        .orderBy(delivery.createdDate.desc())
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize() + 1).fetch();
    boolean hasNext = false;
    if (content.size() > pageable.getPageSize()) {
      content.remove(pageable.getPageSize());
      hasNext = true;
    }
    return new SliceImpl(content, pageable, hasNext);
  }

  @Override
  public Slice<OrderingListResponse> getOwnOrdering(Long memberId, LocalDate today,
      Pageable pageable) {
    List<OrderingListResponse> content = jpaQueryFactory.select(
            Projections.constructor(OrderingListResponse.class,
//                delivery.id,
                order.id,
                order.orderStatus,
                order.summary,
                order.description,
                place.name,
                jpaQueryFactory.select(member.nickname).from(member)
                    .where(member.id.eq(delivery.deliver.id))
            ))
        .from(order)
//        .leftJoin(delivery).on(order.id.eq(delivery.order.id))
//    (delivery.order, order)
        .leftJoin(place).on(order.place.id.eq(place.id))
        .where(
            order.orderer_id.eq(memberId),
            order.orderStatus.eq(OrderStatus.ORDERED)
                .or(order.orderStatus.eq(OrderStatus.DELIVERING)),
//            delivery.deliveryStatus.eq(DeliveryStatus.DELIVERING),
            eqEta(today)
        )
        .orderBy(order.createdDate.desc())
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize() + 1).fetch();
    boolean hasNext = false;
    if (content.size() > pageable.getPageSize()) {
      content.remove(pageable.getPageSize());
      hasNext = true;
    }
    return new SliceImpl(content, pageable, hasNext);
  }

  private BooleanExpression eqEta(LocalDate now) {
    LocalTime start = LocalTime.MIN;
    LocalTime end = LocalTime.MAX;
    return delivery.order.eta.between(now.atTime(start), now.atTime(end));
  }
}
