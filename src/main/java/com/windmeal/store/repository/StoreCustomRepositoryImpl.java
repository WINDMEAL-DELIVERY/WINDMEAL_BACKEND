package com.windmeal.store.repository;

import static com.windmeal.order.domain.QOrder.*;
import static com.windmeal.store.domain.QCategory.category;
import static com.windmeal.store.domain.QStore.store;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.windmeal.order.domain.OrderStatus;
import com.windmeal.order.domain.QOrder;
import com.windmeal.order.dto.response.OrderMapListResponse;
import com.windmeal.store.domain.QStore;
import com.windmeal.store.domain.QStoreCategory;
import com.windmeal.store.dto.response.AllStoreResponse;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

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
    public List<OrderMapListResponse> getStoreMapList(Long storeId, String eta, String storeCategory, Long placeId, OrderStatus orderStatus) {
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
                .leftJoin(order).on(store.id.eq(order.store_id))
                .where(eqStoreId(storeId), eqEta(eta), eqStoreCategory(storeCategory), eqPlace(placeId), eqOrderStatus(orderStatus))
                .groupBy(store.id).fetch();
    }

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

    private BooleanExpression eqPlace(Long placeId) {
        if(placeId==null){
            return null;
        }else{
            return order.place.id.eq(placeId);
        }

    }

    private List<Long> getCategoryIds(String storeCategory) {
        return jpaQueryFactory.select(category.id)
                .from(category)
                .where(category.name.contains(storeCategory)).fetch(); //storeCategory 가 포함되는 category_id 조회
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
}
