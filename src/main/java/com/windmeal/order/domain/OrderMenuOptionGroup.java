package com.windmeal.order.domain;

import com.windmeal.store.domain.OptionGroup;
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
public class OrderMenuOptionGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "order-sequence-generator")
    @Column(name = "order_menu_option_group_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_menu_id", updatable = false)
    private OrderMenu orderMenu;
    private String name;

    @Column(name = "option_group_id")
    private Long option_group_id;
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "orderMenuOptionGroup")
    private List<OrderMenuOptionSpecification> specs = new ArrayList<>();

    @Builder
    public OrderMenuOptionGroup(String name, Long option_group_id,
        List<OrderMenuOptionSpecification> specs) {
        this.name = name;
        this.option_group_id = option_group_id;
        specs.forEach(orderMenuOptionSpecification -> {
            orderMenuOptionSpecification.setOrderMenuOptionGroup(this);
            this.specs.add(orderMenuOptionSpecification);
        });
    }




    public void setOrderMenu(OrderMenu orderMenu) {
        this.orderMenu = orderMenu;
    }
}
