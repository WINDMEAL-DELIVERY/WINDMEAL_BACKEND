package com.windmeal;

import com.windmeal.member.domain.Member;
import com.windmeal.member.domain.Member.MemberBuilder;
import com.windmeal.order.domain.Order;
import com.windmeal.order.domain.Order.OrderBuilder;

public class Fixtures {

  public static OrderBuilder aOrder(){
    return Order.builder()
        .id(1L)
        .orderer_id(1L)
        ;
  }
  public static MemberBuilder aMember(){
    return Member.builder()
        .id(1L);
  }

}
