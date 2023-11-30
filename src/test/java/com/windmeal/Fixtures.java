package com.windmeal;

import com.windmeal.member.domain.Member;
import com.windmeal.member.domain.Member.MemberBuilder;
import com.windmeal.model.place.Place;
import com.windmeal.order.domain.Delivery;
import com.windmeal.order.domain.Order;
import com.windmeal.order.domain.Order.OrderBuilder;
import com.windmeal.store.domain.Menu;
import com.windmeal.store.domain.Menu.MenuBuilder;
import com.windmeal.store.domain.OptionGroup;
import com.windmeal.store.domain.OptionGroup.OptionGroupBuilder;
import com.windmeal.store.domain.OptionSpecification;
import com.windmeal.store.domain.OptionSpecification.OptionSpecificationBuilder;
import com.windmeal.store.domain.Store;
import com.windmeal.store.domain.Store.StoreBuilder;
import java.time.LocalTime;
import java.util.Arrays;

public class Fixtures {

  public static OrderBuilder aOrder(){
    return Order.builder()
        .id(1L)
        .eta(LocalTime.MAX)
        .orderer_id(1L)
        ;
  }
  public static MemberBuilder aMember(){
    return Member.builder()
        ;
  }

  public static StoreBuilder aStore() {
    return Store.builder()
        .name("오겹돼지");
  }

  public static MenuBuilder aMenu() {
    return Menu.builder()
        .optionGroups(Arrays.asList(aOptionGroup().build()))
        ;
  }

  public static OptionGroupBuilder aOptionGroup() {
    return OptionGroup.builder()
        .id(1L)
        .isMultipleOption(true)
        .isEssentialOption(true)
        .optionSpecifications(Arrays.asList(aOptionSpecification().build(),
            aOptionSpecification().build()))
        ;
  }

  public static OptionSpecificationBuilder aOptionSpecification() {
    return OptionSpecification.builder();
  }

  public static Place.PlaceBuilder aPlace(){
    return Place.builder();
  }
  public static Delivery.DeliveryBuilder aDelivery(){
    return Delivery.builder();
  }
}
