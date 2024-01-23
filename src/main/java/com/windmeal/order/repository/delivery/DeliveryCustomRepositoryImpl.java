package com.windmeal.order.repository.delivery;

import static com.windmeal.member.domain.QMember.member;
import static com.windmeal.model.place.QPlace.place;
import static com.windmeal.order.domain.QDelivery.delivery;
import static com.windmeal.order.domain.QOrder.order;
import static com.windmeal.store.domain.QCategory.category;
import static com.windmeal.store.domain.QStore.*;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.windmeal.global.wrapper.RestSlice;
import com.windmeal.order.domain.DeliveryStatus;
import com.windmeal.order.domain.OrderStatus;
import com.windmeal.order.dto.response.DeliveryListResponse;
import com.windmeal.order.dto.response.OrderingListResponse;
import com.windmeal.order.dto.response.OwnDeliveryListResponse;
import com.windmeal.store.domain.QStore;
import com.windmeal.store.domain.QStoreCategory;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

@RequiredArgsConstructor
public class DeliveryCustomRepositoryImpl implements DeliveryCustomRepository {

  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public List<DeliveryListResponse> getOwnDelivering(Long memberId, LocalDate today,
      Pageable pageable) {
//    List<DeliveryListResponse> content =
    return jpaQueryFactory.select(
            Projections.constructor(DeliveryListResponse.class,
                delivery.id,
                order.id,
                delivery.deliveryStatus,
                order.summary,
                order.description,
                place.name,
                jpaQueryFactory.select(member.nickname).from(member)
                    .where(member.id.eq(order.orderer_id)),
                store.name
            ))
        .from(delivery)
        .innerJoin(delivery.order, order)
        .innerJoin(order.place, place)
        .leftJoin(store).on(order.store_id.eq(store.id))
        .where(
            delivery.deliver.id.eq(memberId),
            delivery.deliveryStatus.eq(DeliveryStatus.DELIVERING),
            eqEta(today)
        )
        .orderBy(delivery.createdDate.desc()).fetch();
//        .offset(pageable.getOffset())
//        .limit(pageable.getPageSize() + 1).fetch();
//    boolean hasNext = false;
//    if (content.size() > pageable.getPageSize()) {
//      content.remove(pageable.getPageSize());
//      hasNext = true;
//    }
//    return new SliceImpl(content, pageable, hasNext);
  }

  @Override
  public List<OrderingListResponse> getOwnOrdering(Long memberId, LocalDate today,
      Pageable pageable) {
//    List<OrderingListResponse> content =
    return jpaQueryFactory.select(
            Projections.constructor(OrderingListResponse.class,
                order.id,
                order.orderStatus,
                order.summary,
                order.description,
                place.name,
                jpaQueryFactory.select(member.nickname).from(member)
                    .where(member.id.eq(delivery.deliver.id)),
                store.name
            ))
        .from(delivery)
//        .leftJoin(delivery).on(order.id.eq(delivery.order.id))
        .rightJoin(delivery.order, order)
        .innerJoin(order.place, place)
        .leftJoin(store).on(order.store_id.eq(store.id))
//        .innerJoin(delivery.deliver,member)

//        .innerJoin(delivery.deliver,member)

        .where(
            order.orderer_id.eq(memberId),
            order.orderStatus.eq(OrderStatus.ORDERED)
                .or(order.orderStatus.eq(OrderStatus.DELIVERING)),
//            delivery.deliveryStatus.eq(DeliveryStatus.DELIVERING),
            eqEta(today)
        )
        .orderBy(order.createdDate.desc()).fetch();
//        .offset(pageable.getOffset())
//        .limit(pageable.getPageSize() + 1).fetch();
//    boolean hasNext = false;
//    if (content.size() > pageable.getPageSize()) {
//      content.remove(pageable.getPageSize());
//      hasNext = true;
//    }
//    return new SliceImpl(content, pageable, hasNext);
  }

  @Override
  public Slice<OwnDeliveryListResponse> getOwnDelivered(Long memberId, Pageable pageable,
      LocalDate startDate, LocalDate endDate, String storeCategory) {

    List<OwnDeliveryListResponse> content = jpaQueryFactory.select(
            Projections.constructor(OwnDeliveryListResponse.class,
                order.id,
                store.id,
                store.name,
                store.photo,
                order.summary,
                delivery.deliveryStatus,
                delivery.createdDate)
        ).
        from(delivery)
        .leftJoin(order).on(delivery.order.id.eq(order.id))
        .leftJoin(store).on(order.store_id.eq(store.id))
        .where(
            delivery.deliver.id.eq(memberId),
            eqDeliveryStatus(),
            eqStoreCategory(storeCategory), //카테고리 필터링
            eqDeliveryDate(startDate, endDate) //날짜 필터링
        ).orderBy(delivery.createdDate.desc())
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize() + 1).fetch();

    boolean hasNext = false;
    if (content.size() > pageable.getPageSize()) {
      content.remove(pageable.getPageSize());
      hasNext = true;
    }
    return new RestSlice(new SliceImpl(content, pageable, hasNext));
  }


  @Override
  public Integer getOwnDeliveredTotalPrice(Long memberId) {

    return jpaQueryFactory.select(order.deliveryFee.price.sum())
        .from(delivery)
        .leftJoin(order).on(delivery.order.id.eq(order.id))
        .where(
            delivery.deliver.id.eq(memberId),
            delivery.deliveryStatus.eq(DeliveryStatus.DELIVERED)
        ).fetchFirst();
  }


  private BooleanExpression eqEta(LocalDate now) {
    LocalTime start = LocalTime.MIN;
    LocalTime end = LocalTime.MAX;
    return delivery.order.eta.between(now.atTime(start), now.atTime(end));
  }

  private BooleanExpression eqDeliveryStatus() {
    return delivery.deliveryStatus.eq(DeliveryStatus.DELIVERED)
        .or(delivery.deliveryStatus.eq(DeliveryStatus.CANCELED));
  }

  //음식 종류로 검색(text로 검색 예정)
  private BooleanExpression eqStoreCategory(String storeCategory) {
    if (storeCategory == null) {
      return null;
    } else {
      return order.store_id.in(
          jpaQueryFactory
              .select(QStoreCategory.storeCategory.store.id)
              .from(QStoreCategory.storeCategory)
              .where(QStoreCategory.storeCategory.category.id.in(getCategoryIds(storeCategory)))
              .fetch());
    }

  }

  private List<Long> getCategoryIds(String storeCategory) {
    return jpaQueryFactory.select(category.id)
        .from(category)
        .where(category.name.contains(storeCategory)).fetch(); //storeCategory 가 포함되는 category_id 조회
  }

  private BooleanExpression eqDeliveryDate(LocalDate startDate, LocalDate endDate) {
    if (startDate == null && endDate == null) //조건 X
    {
      return null;
    }

    if (startDate == null) { //시작날짜만 조건이 있는 경우
      return delivery.createdDate.lt(endDate.plusDays(1).atStartOfDay());
    }

    if (endDate == null) { //마지막 날짜 조건만 있는 경우
      return delivery.createdDate.goe(startDate.atStartOfDay());
    }

    return delivery.createdDate.goe(startDate.atStartOfDay())
        .and(delivery.createdDate.lt(endDate.plusDays(1).atStartOfDay()));
  }

  private static BooleanExpression eqOrderStatus() {
    return order.orderStatus.eq(OrderStatus.DELIVERED)
        .or(order.orderStatus.eq(OrderStatus.CANCELED));
  }
}
