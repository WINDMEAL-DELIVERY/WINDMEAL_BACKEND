package com.windmeal.store.domain;

import com.windmeal.generic.domain.Money;
import com.windmeal.order.dto.request.OrderCreateRequest.OrderSpecRequest;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OptionSpecification {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "option_specification_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "option_group_id")
  private OptionGroup optionGroup;

  private String name;

  @Embedded
  private Money price;

  @Builder

  public OptionSpecification(Long id, OptionGroup optionGroup, String name, Money price) {
    this.id = id;
    this.optionGroup = optionGroup;
    this.name = name;
    this.price = price;
  }
  @Override
  public boolean equals(Object object) {
    if (object == null) {
      return false;
    }

    if (!(object instanceof OptionSpecification)) {
      return false;
    }

    OptionSpecification other = (OptionSpecification)object;
    return Objects.equals(id, other.getId())&&Objects.equals(name, other.getName()) && Objects.equals(price, other.getPrice());
  }

  @Override
  public int hashCode() {
    return Objects.hash(id,name, price);
  }

  public boolean isSatisfiedBy(OrderSpecRequest option) {
    return Objects.equals(id,option.getOptionSpecId())&&Objects.equals(name, option.getName()) && Objects.equals(price, option.getPrice());
  }
}
