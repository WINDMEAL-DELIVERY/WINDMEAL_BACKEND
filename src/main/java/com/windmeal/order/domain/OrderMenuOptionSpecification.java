package com.windmeal.order.domain;


import com.windmeal.generic.domain.Money;
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
    allocationSize = 10)
public class OrderMenuOptionSpecification {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "order-sequence-generator")
    @Column(name = "order_menu_option_specification_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_menu_option_group_id", updatable = false)
    private OrderMenuOptionGroup orderMenuOptionGroup;


    @Column(name = "option_specification_id")
    private Long option_specification_id;

    private Money price;
    private String name;

    @Builder
    public OrderMenuOptionSpecification(Long option_specification_id, Money price, String name) {
        this.option_specification_id = option_specification_id;
        this.price = price;
        this.name = name;
    }



    public void setOrderMenuOptionGroup(OrderMenuOptionGroup orderMenuOptionGroup) {
        this.orderMenuOptionGroup = orderMenuOptionGroup;
    }
}
