package com.windmeal.order.domain;

import com.windmeal.member.domain.Member;
import com.windmeal.model.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Delivery extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_id")
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", updatable = false)
    private Member deliver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", updatable = false)
    private Order order;

    @Enumerated(value = EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    @Builder
    public Delivery(Member deliver, Order order, DeliveryStatus deliveryStatus) {
        this.deliver = deliver;
        this.order = order;
        this.deliveryStatus = deliveryStatus;
    }


    private void delivering(){
        this.deliveryStatus=DeliveryStatus.DELIVERING;
    }

    public void canceled() {
        this.deliveryStatus=DeliveryStatus.CANCELED;
    }
}
