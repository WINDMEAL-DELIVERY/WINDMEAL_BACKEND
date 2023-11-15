package com.windmeal.order.domain;


import com.windmeal.generic.domain.Money;
import com.windmeal.member.domain.Member;
import com.windmeal.model.BaseTimeEntity;
import com.windmeal.store.domain.Store;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.id.enhanced.SequenceStyleGenerator;
import org.springframework.data.geo.Point;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders")
@SequenceGenerator(name = "order-sequence-generator",
    sequenceName = "order_seq", //매핑할 데이터베이스 시퀀스 이름
    initialValue = 1,
    allocationSize = 1)
public class Order extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "order-sequence-generator")
  @Column(name = "order_id")
  private Long id;

  @Column(name = "member_id")
  private Long orderer_id;

  @Column(name = "store_id")
  private Long store_id;
  @Enumerated(value = EnumType.STRING)
  private OrderStatus orderStatus;

  private LocalDateTime orderTime;

  private Point destination;//도착지

  private LocalTime eta; //Estimated Time of Arrival 도착 예정 시간

  private String summary; //내용 요약 ex) 후라이드 치킨 1마리 외 3개 15000원

  @Embedded
  @AttributeOverride(name = "price", column = @Column(name = "delivery_fee"))
  private Money deliveryFee;

  @Embedded
  @AttributeOverride(name = "price", column = @Column(name = "total_price"))
  private Money totalPrice;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "order")
  private List<OrderMenu> orderMenus = new ArrayList<>();

  public Order(Long orderer, Long store, LocalDateTime orderTime, String summary, Money deliveryFee,
      List<OrderMenu> orderMenus, Money totalPrice) {
    this(null, orderer, store, OrderStatus.ORDERED, orderTime, summary, deliveryFee, orderMenus,
        totalPrice);
  }

  @Builder
  public Order(Long id, Long orderer, Long store, OrderStatus orderStatus,
      LocalDateTime orderTime,
      String summary, Money deliveryFee, List<OrderMenu> orderMenus, Money totalPrice) {
    this.id = id;
    this.orderer_id = orderer;
    this.store_id = store;
    this.orderStatus = orderStatus;
    this.orderTime = orderTime;
    this.summary = summary;
    this.deliveryFee = deliveryFee;
    orderMenus.forEach(orderMenu -> {
      orderMenu.setOrder(this);
      this.orderMenus.add(orderMenu);
    });
    this.totalPrice = totalPrice;
  }
}
