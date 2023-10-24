package com.windmeal.store.domain;

import com.windmeal.generic.domain.Money;
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
  public OptionSpecification(OptionGroup optionGroup, String name, Money price) {
    this.optionGroup = optionGroup;
    this.name = name;
    this.price = price;
  }
}
