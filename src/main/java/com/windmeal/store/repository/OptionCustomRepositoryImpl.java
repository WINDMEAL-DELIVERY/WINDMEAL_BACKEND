package com.windmeal.store.repository;


import static com.windmeal.store.domain.QMenu.menu;
import static com.windmeal.store.domain.QOptionSpecification.optionSpecification;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.windmeal.store.dto.request.OptionCreateRequest.OptionSpecRequest;
import com.windmeal.store.dto.response.MenuResponse;
import com.windmeal.store.dto.response.OptionSpecResponse;
import java.sql.PreparedStatement;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;

@RequiredArgsConstructor
public class OptionCustomRepositoryImpl implements OptionCustomRepository {

  private final JdbcTemplate jdbcTemplate;
  private final JPAQueryFactory jpaQueryFactory;
  @Override
  public void createOptionSpecs(List<OptionSpecRequest> optionSpecifications,
      Long option_group_id) {
    String sql = "INSERT INTO option_specification (option_group_id,name,price)\n"
        + "VALUES (?, ?,?)";

    jdbcTemplate.batchUpdate(sql,
        optionSpecifications,
        optionSpecifications.size(),
        (PreparedStatement ps, OptionSpecRequest optionSpec) -> {
          ps.setLong(1, option_group_id);
          ps.setString(2, optionSpec.getName());
          ps.setInt(3, optionSpec.getPrice());
        });
  }

  @Override
  public List<OptionSpecResponse> findByOptionGroupIdIn(List<Long> optionGroupIdList) {
    return jpaQueryFactory.select(Projections.constructor(
            OptionSpecResponse.class,
            optionSpecification.optionGroup.id,
            optionSpecification.id,
            optionSpecification.name,
            optionSpecification.price
        ))
        .from(optionSpecification)
        .where(optionSpecification.optionGroup.id.in(optionGroupIdList)).fetch();

  }
}
