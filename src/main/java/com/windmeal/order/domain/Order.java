package com.windmeal.order.domain;


import com.windmeal.generic.domain.Money;
import com.windmeal.member.domain.Member;
import com.windmeal.model.BaseTimeEntity;
import com.windmeal.store.domain.Store;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders")
public class Order extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", updatable = false)
    private Member orderer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", updatable = false)
    private Store store;

    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;

    private LocalDateTime orderTime;

    private String summary; //내용 요약 ex) 후라이드 치킨 1마리 외 3개 15000원

    private Money deliveryFee;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="order_id")
    private List<OrderMenu> orderMenus = new ArrayList<>();


    public Order (Member orderer,Store store, LocalDateTime orderTime, String summary, Money deliveryFee,List<OrderMenu> orderMenus) {
        this(null,orderer,store,OrderStatus.ORDERED,orderTime,summary,deliveryFee,orderMenus);
    }
    @Builder
    public Order(Long id, Member orderer, Store store, OrderStatus orderStatus,
        LocalDateTime orderTime,
        String summary, Money deliveryFee, List<OrderMenu> orderMenus) {
        this.id = id;
        this.orderer = orderer;
        this.store = store;
        this.orderStatus = orderStatus;
        this.orderTime = orderTime;
        this.summary = summary;
        this.deliveryFee = deliveryFee;
        this.orderMenus.addAll(orderMenus);
    }
}
