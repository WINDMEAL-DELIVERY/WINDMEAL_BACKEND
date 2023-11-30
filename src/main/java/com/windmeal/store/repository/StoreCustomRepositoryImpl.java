package com.windmeal.store.repository;

import static com.windmeal.store.domain.QStore.store;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.windmeal.store.dto.response.AllStoreResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

@RequiredArgsConstructor
public class StoreCustomRepositoryImpl implements StoreCustomRepository{
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
}
