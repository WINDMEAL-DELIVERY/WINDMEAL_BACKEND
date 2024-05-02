package com.windmeal.generic.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Function;
import javax.persistence.Embeddable;
import lombok.Getter;


@Embeddable
@Getter
public class Money {

  public static final Money ZERO = Money.wons(0);
  public static final Money MIN = Money.wons(1000);

  private int price;

  private Money(int price) {
    this.price = price;
  }


  public Money() {

  }



  public int wons() {
    return this.price;
  }

  public static Money wons(int amount) {
    return new Money((amount));
  }

  public static <T> Money sum(Collection<T> bags, Function<T, Money> monetary) {
    return bags.stream().map(bag -> monetary.apply(bag)).reduce(Money.ZERO, Money::plus);
  }

  public Money plus(Money amount) {
    return new Money(this.price+(amount.wons()));
  }

  public Money minus(Money amount) {
    return new Money(this.price-(amount.wons()));
  }

  //
  public Money times(int percent) {
    return new Money(this.price * percent);
  }

  public boolean isLessThan(Money other) {
    return this.price-(other.wons()) < 0;
  }

  //
  public boolean isGreaterThanOrEqual(Money other) {
    return price-(other.wons()) >= 0;
  }
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof Money)) {
            return false;
        }

        Money other = (Money) object;
        return Objects.equals(price, other.getPrice());
    }

    public int hashCode() {
        return Objects.hashCode(price);
    }
}