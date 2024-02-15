package com.windmeal.order.repository.order;

import static com.windmeal.member.domain.QBlackList.blackList;
import static com.windmeal.order.domain.QOrder.order;
import static com.windmeal.store.domain.QCategory.category;
import static com.windmeal.store.domain.QStore.*;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.windmeal.global.wrapper.RestSlice;
import com.windmeal.member.domain.QMember;
import com.windmeal.order.domain.OrderStatus;
import com.windmeal.order.dto.response.OrderListResponse;
import com.windmeal.order.dto.response.OrderMapListResponse;
import com.windmeal.order.dto.response.OwnOrderListResponse;
import com.windmeal.store.domain.QStore;
import com.windmeal.store.domain.QStoreCategory;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

@RequiredArgsConstructor
public class OrderCustomRepositoryImpl implements OrderCustomRepository {


  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public List<OrderMapListResponse> getOrderMapList(Long storeId, String eta,
      String storeCategory, Long placeId, OrderStatus orderStatus){

    return jpaQueryFactory.select(Projections.constructor(
        OrderMapListResponse.class,
            order.store_id,
            store.name,
            order.store_id.count(),
            store.place.longitude,
            store.place.latitude
        ))
        .from(order)
        .leftJoin(store).on(store.id.eq(order.store_id))
        .where(eqStoreId(storeId), eqEta(eta), eqStoreCategory(storeCategory), eqPlace(placeId),eqOrderStatus(orderStatus))
        .groupBy(order.store_id).fetch();
  }

  @Override
  public RestSlice<OrderListResponse> getOrderList(Pageable pageable, Long storeId, String eta,
      String storeCategory, Long placeId, Long memberId) {
    List<OrderListResponse> content = jpaQueryFactory.select(Projections.constructor(
            OrderListResponse.class,
            order.id,
            QMember.member.id,
            QMember.member.nickname,
            order.place.name,
            order.place.longitude,
            order.place.latitude,
            order.eta,
            order.deliveryFee,
            store.name,
            order.summary
        ))
        .from(order)
        .leftJoin(QMember.member).on(QMember.member.id.eq(order.orderer_id))
        .leftJoin(store).on(order.store_id.eq(store.id))
        .where(eqStoreId(storeId), eqEta(eta), eqStoreCategory(storeCategory), eqPlace(placeId),eqBlackList(memberId),eqOrderStatus(OrderStatus.ORDERED))
        .orderBy(order.createdDate.desc())
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
  public Integer getOwnOrderedTotalPrice(Long memberId) {

    return jpaQueryFactory.select(order.deliveryFee.price.subtract(3000).sum())
        .from(order)
        .where(
            order.orderer_id.eq(memberId),
            order.orderStatus.eq(OrderStatus.DELIVERED)
        ).fetchFirst();
  }

  @Override
  public Slice<OwnOrderListResponse> getOwnOrdered(Long memberId, Pageable pageable, LocalDate startDate,LocalDate endDate, String storeCategory) {
    List<OwnOrderListResponse> content = jpaQueryFactory.select(
            Projections.constructor(OwnOrderListResponse.class,
                order.id,
                store.id,
                store.name,
                store.photo,
                order.summary,
                order.orderStatus,
                order.orderTime
            )
        )
        .from(order)
        .leftJoin(store).on(order.store_id.eq(store.id))
        .where(
            order.orderer_id.eq(memberId),
            eqOrderStatus(),
            eqStoreCategory(storeCategory), //카테고리 필터링
            eqOrderDate(startDate,endDate) //날짜 필터링

        ).orderBy(order.createdDate.desc())
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize() + 1).fetch();

    boolean hasNext = false;
    if (content.size() > pageable.getPageSize()) {
      content.remove(pageable.getPageSize());
      hasNext = true;
    }
    return new RestSlice(new SliceImpl(content, pageable, hasNext));
  }

  private BooleanExpression eqOrderDate(LocalDate startDate, LocalDate endDate) {
    if(startDate==null&&endDate==null) //조건 X
    {
      return null;
    }

    if(startDate==null){ //시작날짜만 조건이 있는 경우
      return order.orderTime.lt(endDate.plusDays(1).atStartOfDay());
    }

    if(endDate==null){ //마지막 날짜 조건만 있는 경우
      return order.orderTime.goe(startDate.atStartOfDay());
    }

    return order.orderTime.goe(startDate.atStartOfDay())
        .and(order.orderTime.lt(endDate.plusDays(1).atStartOfDay()));
  }

  private static BooleanExpression eqOrderStatus() {
    return order.orderStatus.eq(OrderStatus.DELIVERED)
        .or(order.orderStatus.eq(OrderStatus.CANCELED));
  }

  private BooleanExpression eqPlace(Long placeId) {
    if(placeId==null){
      return null;
    }else{
      return order.place.id.eq(placeId);
    }

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

  //도착 시간으로 검색
  private BooleanExpression eqEta(String eta) {
    LocalDate now = LocalDate.now();
    LocalTime start = LocalTime.MIN;
    LocalTime end = LocalTime.MAX;
    if (eta != null) {
      start = LocalTime.parse(eta);
      end = LocalTime.parse(eta).plus(1, ChronoUnit.MINUTES);
    }
    return order.eta.between(now.atTime(start),now.atTime(end));
  }


  private BooleanExpression eqStoreId(Long storeId) {
    if (storeId != null) {
      return order.store_id.eq(storeId);
    }
    return null;
  }

  private BooleanExpression eqOrderStatus(OrderStatus orderStatus) {
    if(orderStatus != null) {
      return order.orderStatus.eq(orderStatus);
    }
    return null;
  }

  private BooleanExpression eqBlackList(Long memberId) {
    if(memberId!=null){
      return order.orderer_id.notIn(
          jpaQueryFactory.select(blackList.requester.id)
              .from(blackList)
              .where(blackList.blacked.id.eq(memberId)).fetch());
    }
    return null;
  }
}
