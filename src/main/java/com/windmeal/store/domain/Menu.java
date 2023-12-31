package com.windmeal.store.domain;

import com.windmeal.generic.domain.Money;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import org.hibernate.annotations.BatchSize;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Menu {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "menu_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "menu_category_id")
  private MenuCategory menuCategory;

  private String name;
  private String description;

  @Embedded
  private Money price;

  private String photo;

  @BatchSize(size = 10)
  @OneToMany(mappedBy = "menu")
  private List<OptionGroup> optionGroups = new ArrayList<>();

  @Builder
  public Menu(MenuCategory menuCategory, String name, String description, Money price, String photo,
      List<OptionGroup> optionGroups) {
    this.menuCategory = menuCategory;
    this.name = name;
    this.description = description;
    this.price = price;
    this.photo = photo;
    this.optionGroups = optionGroups;
  }

  @Builder
  public Menu(Long id) {
    this.id = id;
  }
}
