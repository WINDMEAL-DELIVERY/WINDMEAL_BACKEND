package com.windmeal.order.repository;

import static com.windmeal.order.domain.QOrder.order;
import static com.windmeal.store.domain.QCategory.category;
import static com.windmeal.store.domain.QStore.*;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.windmeal.order.dto.response.OrderListResponse;
import com.windmeal.store.domain.QStoreCategory;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.Point;

@RequiredArgsConstructor
public class OrderCustomRepositoryImpl implements OrderCustomRepository {


  private final JPAQueryFactory jpaQueryFactory;



  @Override
  public Page<OrderListResponse> getOrderList(Pageable pageable, Long storeId, String eta,
      String storeCategory, Point point) {
    JPAQuery<OrderListResponse> query = jpaQueryFactory.select(Projections.constructor(
            OrderListResponse.class,
            order.id,
            order.destination,
            order.eta,
            order.deliveryFee,
            store.name,
            order.summary
        ))
        .from(order)
        .leftJoin(store).on(order.store_id.eq(store.id))
        .where(eqStoreId(storeId), eqEta(eta), eqStoreCategory(storeCategory),eqPoint(point));


    long size = query.fetchCount();
    List<OrderListResponse> result = query.fetch();
    return new PageImpl<>(result, pageable, size);
  }

  private BooleanExpression eqPoint(Point point) {
    if(point==null){
      return null;
    }else{
      return store.location.eq(point);
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
}
