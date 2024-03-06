package com.windmeal.store.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.windmeal.order.domain.OrderStatus;
import com.windmeal.order.dto.response.OrderMapListResponse;
import com.windmeal.store.domain.QStoreCategory;
import com.windmeal.store.dto.response.AllStoreResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.windmeal.order.domain.QOrder.order;
import static com.windmeal.store.domain.QCategory.category;
import static com.windmeal.store.domain.QStore.store;

@Slf4j
@RequiredArgsConstructor
public class StoreCustomRepositoryImpl implements StoreCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Slice<AllStoreResponse> getAllStoreInfo(Pageable pageable) {
        List<AllStoreResponse> content = jpaQueryFactory.select(
                Projections.constructor(AllStoreResponse.class,
                    store.id,
                    store.name)
            )
            .from(store)
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
    public List<OrderMapListResponse> getStoreMapList(Long storeId, String eta,
        String storeCategory, Long placeId, OrderStatus orderStatus, Boolean isOpen) {
        return jpaQueryFactory
            .select(Projections.constructor(
                OrderMapListResponse.class,
                store.id,
                store.name,
                order.id.count(),
                store.place.longitude,
                store.place.latitude
            ))
            .from(store)
            .leftJoin(order).on(store.id.eq(order.store_id)).on(
                eqOrderStatus(orderStatus), eqOrderTime(LocalDateTime.now().toString()))
            .where(eqStoreId(storeId), eqEta(eta), eqStoreCategory(storeCategory),
                eqPlace(placeId), eqOpen(isOpen))
            .groupBy(store.id).fetch();
    }

    private BooleanExpression eqEta(String eta) {
//        LocalTime start = LocalTime.MIN;
//        LocalTime end = LocalTime.MAX;
        if (eta != null) {
            LocalDate now = LocalDate.now();
            LocalTime start = LocalTime.parse(eta);
            LocalTime end = LocalTime.parse(eta).plus(10, ChronoUnit.MINUTES);
            return order.eta.between(now.atTime(start),now.atTime(end));
        }
        return null;
    }

    private BooleanExpression eqOrderTime(String orderTime) {
        //  "orderTime": "2024-03-06T02:16:06.68306",
        // 오늘 날짜만 반환
        if(orderTime != null) {
            StringExpression formattedDate = Expressions.stringTemplate(
                "FUNCTION('DATE_FORMAT', {0}, '%Y-%m-%d')", orderTime);
            StringExpression orderTimeFormattedDate = Expressions.stringTemplate(
                "FUNCTION('DATE_FORMAT', {0}, '%Y-%m-%d')", order.orderTime);
            return formattedDate.eq(orderTimeFormattedDate);
        }
        return null;
    }


    private BooleanExpression eqStoreId(Long storeId) {
        if (storeId != null) {
            return order.store_id.eq(storeId);
        }
        return null;
    }

    private BooleanExpression eqOrderStatus(OrderStatus orderStatus) {
        if (orderStatus != null) {
            return order.orderStatus.eq(orderStatus);
        }
        return null;
    }

    private BooleanExpression eqPlace(Long placeId) {
        if (placeId == null) {
            return null;
        } else {
            return order.place.id.eq(placeId);
        }

    }

    private List<Long> getCategoryIds(String storeCategory) {
        return jpaQueryFactory.select(category.id)
            .from(category)
            .where(category.name.contains(storeCategory)).fetch();//storeCategory 가 포함되는 category_id 조회
    }

    //음식 종류로 검색(text로 검색 예정)
    private BooleanExpression eqStoreCategory(String storeCategory) {
        if (storeCategory == null) {
            return null;
        } else {
            List<Long> categoryIds = getCategoryIds(storeCategory);
            if (!categoryIds.isEmpty()) {
                return store.id.in(
                    // 없는 카테고리에 대해서는 바로 예외가 발생한다.
                    jpaQueryFactory
                        .select(QStoreCategory.storeCategory.store.id)
                        .from(QStoreCategory.storeCategory)
                        .where(QStoreCategory.storeCategory.category.id.in(categoryIds))
                        .fetch());
            }
        }
        /*
        Expressions.FLASE 를 사용하면 자꾸 SQL 에러가 발생했다.
        그래서 아래와 같이 임의로 false 조건을 만들어 전달했다.
        더 괜찮은 방법을 찾아야 할 듯 하다.
         */
        return store.id.eq(-1l);
    }

    private BooleanExpression eqOpen(Boolean isOpen) {
        if(isOpen) {
            LocalTime currentTime = LocalTime.now(ZoneId.of("Asia/Seoul"));
            return store.closeTime.after(currentTime);
        }
        return null;
    }
}
