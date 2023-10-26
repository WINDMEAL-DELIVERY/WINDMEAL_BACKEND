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


  @Builder
  public Menu(MenuCategory menuCategory, String name, String description, Money price) {
    this.menuCategory = menuCategory;
    this.name = name;
    this.description = description;
    this.price = price;
  }
}
