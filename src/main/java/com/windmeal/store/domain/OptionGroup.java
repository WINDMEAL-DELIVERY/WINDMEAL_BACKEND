package com.windmeal.store.domain;

import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OptionGroup {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "option_group_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "menu_id")
  private Menu menu;

  private String name;

  private Boolean isEssentialOption; //필수 선택 여부

  private Boolean isMultipleOption; //다중 선택 여부

  @OneToMany(mappedBy = "optionGroup")
  private List<OptionSpecification> optionSpecifications = new ArrayList<>();
  @Builder

  public OptionGroup(Long id, Menu menu, String name, Boolean isEssentialOption,
      Boolean isMultipleOption, List<OptionSpecification> optionSpecifications) {
    this.id = id;
    this.menu = menu;
    this.name = name;
    this.isEssentialOption = isEssentialOption;
    this.isMultipleOption = isMultipleOption;
    this.optionSpecifications = optionSpecifications;
  }
}
