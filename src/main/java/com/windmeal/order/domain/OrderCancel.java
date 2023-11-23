package com.windmeal.order.domain;

import com.windmeal.member.domain.Member;
import com.windmeal.model.BaseEntity;
import com.windmeal.model.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderCancel extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_cancel_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", updatable = false)
    private Order order;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id", updatable = false)
    private Delivery delivery;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", updatable = false)
    private Member cancelMember;

    private String content;

    @Builder
    public OrderCancel(Order order, Delivery delivery, Member cancelMember, String content) {
        this.order = order;
        this.delivery = delivery;
        this.cancelMember = cancelMember;
        this.content = content;
        order.canceled();
        delivery.canceled();
    }
}
