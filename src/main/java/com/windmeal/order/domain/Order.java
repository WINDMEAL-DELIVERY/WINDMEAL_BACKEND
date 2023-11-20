package com.windmeal.order.domain;


import com.windmeal.generic.domain.Money;
import com.windmeal.model.BaseTimeEntity;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
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

  public Order(Long orderer, Long store, LocalDateTime orderTime, Point destination, LocalTime eta,  Money deliveryFee,
      List<OrderMenu> orderMenus) {
    this(null, orderer, store, null, orderTime,destination,eta,  deliveryFee, orderMenus);
  }

  @Builder
  public Order(Long id, Long orderer_id, Long store_id, OrderStatus orderStatus,
      LocalDateTime orderTime, Point destination, LocalTime eta,  Money deliveryFee,
      List<OrderMenu> orderMenus) {
    this.id = id;
    this.orderer_id = orderer_id;
    this.store_id = store_id;
    this.orderStatus = orderStatus;
    this.orderTime = orderTime;
    this.destination = destination;
    this.eta = eta;
    this.deliveryFee = deliveryFee;
    orderMenus.forEach(orderMenu -> {
      orderMenu.setOrder(this);
      this.orderMenus.add(orderMenu);
    });
  }




  public void place(Money totalPrice, String summary){
      this.totalPrice = totalPrice;
      this.summary = summary;
      ordered();
  }

  private void ordered(){
    this.orderStatus=OrderStatus.ORDERED;
  }

  public void delivering(){
    this.orderStatus=OrderStatus.DELIVERING;
  }

  public void delivered(){
    this.orderStatus=OrderStatus.DELIVERED;
  }


}
