package com.windmeal.order.domain;

import com.windmeal.member.domain.Member;
import com.windmeal.store.domain.Menu;
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
@SequenceGenerator(name = "order-sequence-generator",
    sequenceName = "order_seq", //매핑할 데이터베이스 시퀀스 이름
    initialValue = 1,
    allocationSize = 5)
public class OrderMenu {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "order-sequence-generator")
  @Column(name = "order_menu_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "order_id", updatable = false)
  private Order order;

  @Column(name = "menu_id")
  private Long menu_id;
  private int count;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "orderMenu")
  private List<OrderMenuOptionGroup> groups = new ArrayList<>();

  @Builder
  public OrderMenu(Long menu_id, int count, List<OrderMenuOptionGroup> groups) {
    this.menu_id = menu_id;
    this.count = count;
    groups.forEach(orderMenuOptionGroup -> {
      orderMenuOptionGroup.setOrderMenu(this);
      this.groups.add(orderMenuOptionGroup);
    });
  }


  public void setOrder(Order order) {
    this.order = order;
  }

}
