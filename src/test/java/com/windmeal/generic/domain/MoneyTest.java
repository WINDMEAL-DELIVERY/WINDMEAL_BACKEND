package com.windmeal.generic.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MoneyTest {

  @DisplayName("")
  @Test
  void MoneyMinus(){
      //given
    Money money_1000_1 = Money.wons(1000);
    Money money_1000_2 = Money.wons(1000);
    //when

    //then
    Assertions.assertThat(money_1000_1.minus(money_1000_2).wons().equals(Money.ZERO.wons())).isTrue();
  }


  @DisplayName("")
  @Test
  void MoneyLessThan(){
    //given
    Money money_1000_1 = Money.wons(1000);
    Money money_1000_2 = Money.wons(1000);
    //when

    //then
    Assertions.assertThat(money_1000_1.isLessThan(money_1000_2)).isFalse();
  }


  @DisplayName("")
  @Test
  void MoneyLessThanFail(){
    //given
    Money money_1000_1 = Money.wons(2000);
    Money money_1000_2 = Money.wons(1000);
    //when

    //then
    Assertions.assertThat(money_1000_1.isLessThan(money_1000_2)).isFalse();
  }

  @DisplayName("")
  @Test
  void MoneyisGreaterThanOrEqual(){
    //given
    Money money_1000_1 = Money.wons(2000);
    Money money_1000_2 = Money.wons(1000);
    //when

    //then
    Assertions.assertThat(money_1000_1.isGreaterThanOrEqual(money_1000_2)).isTrue();
  }
}