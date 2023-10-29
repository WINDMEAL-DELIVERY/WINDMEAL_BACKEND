package com.windmeal.store.repository;


import com.windmeal.store.dto.request.OptionCreateRequest.OptionSpecRequest;
import java.sql.PreparedStatement;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;

@RequiredArgsConstructor
public class OptionCustomRepositoryImpl implements OptionCustomRepository {

  private final JdbcTemplate jdbcTemplate;

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
}
