package com.windmeal.order.domain;


import com.windmeal.generic.domain.Money;
import com.windmeal.model.BaseTimeEntity;
import com.windmeal.model.event.EventPublisher;
import com.windmeal.model.place.Place;
import com.windmeal.order.domain.event.DeliveryCancelEvent;
import com.windmeal.order.domain.event.DeliveryMatchEvent;
import java.time.LocalDate;
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

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "place_id")
  private Place place;

  private String description;

  private LocalDateTime eta; //Estimated Time of Arrival 도착 예정 시간

  private String summary; //내용 요약 ex) 후라이드 치킨 1마리 외 3개 15000원

  @Embedded
  @AttributeOverride(name = "price", column = @Column(name = "delivery_fee"))
  private Money deliveryFee;

  @Embedded
  @AttributeOverride(name = "price", column = @Column(name = "total_price"))
  private Money totalPrice;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "order")
  private List<OrderMenu> orderMenus = new ArrayList<>();

  public Order(Long orderer, Long store,String description, LocalDateTime orderTime,  LocalTime eta,  Money deliveryFee,
      List<OrderMenu> orderMenus) {
    this(null, orderer, store,description, null, orderTime,eta,  deliveryFee, orderMenus);
  }

  @Builder
  public Order(Long id, Long orderer_id, Long store_id,String description, OrderStatus orderStatus,
      LocalDateTime orderTime,  LocalTime eta,  Money deliveryFee,
      List<OrderMenu> orderMenus) {
    this.id = id;
    this.orderer_id = orderer_id;
    this.store_id = store_id;
    this.description=description;
    this.orderStatus = orderStatus;
    this.orderTime = orderTime;
    this.eta = LocalDate.now().atTime(eta);
    this.deliveryFee = deliveryFee;
    orderMenus.forEach(orderMenu -> {
      orderMenu.setOrder(this);
      this.orderMenus.add(orderMenu);
    });
  }




  public void place(Money totalPrice, String summary,Place place){
      this.totalPrice = totalPrice;
      this.summary = summary;
      this.place = place;
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

  public void canceled(){
    this.orderStatus=OrderStatus.CANCELED;
  }


  public void updateDeliveryFee(Money deliveryFee) {
    this.deliveryFee = deliveryFee;
  }
}
