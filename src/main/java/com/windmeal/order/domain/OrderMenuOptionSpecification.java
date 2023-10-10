package com.windmeal.order.domain;


import com.windmeal.store.domain.OptionSpecification;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderMenuOptionSpecification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_menu_option_specification_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_menu_option_group_id", updatable = false)
    private OrderMenuOptionGroup orderMenuOptionGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_specification_id", updatable = false)
    private OptionSpecification optionSpecification;
}
