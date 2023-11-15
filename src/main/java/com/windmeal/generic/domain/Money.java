package com.windmeal.generic.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.function.Function;
import javax.persistence.Embeddable;
import lombok.Getter;


@Embeddable
@Getter
@JsonDeserialize(using = MoneyDeserializer.class)
public class Money {

  public static final Money ZERO = Money.wons(0);
  public static final Money MIN = Money.wons(1000);

  private BigDecimal price;

  private Money(BigDecimal price) {
    this.price = price;
  }

  public Money() {

  }


  public BigDecimal wons() {
    return this.price;
  }

  public static Money wons(int amount) {
    return new Money(BigDecimal.valueOf(amount));
  }

  public static <T> Money sum(Collection<T> bags, Function<T, Money> monetary) {
    return bags.stream().map(bag -> monetary.apply(bag)).reduce(Money.ZERO, Money::plus);
  }

  public Money plus(Money amount) {
    return new Money(this.price.add(amount.wons()));
  }

  public Money minus(Money amount) {
    return new Money(this.price.subtract(amount.wons()));
  }

  //
  public Money times(double percent) {
    return new Money(this.price.multiply(BigDecimal.valueOf(percent)));
  }

  public boolean isLessThan(Money other) {
    return this.price.compareTo(other.wons()) < 0;
  }

  //
  public boolean isGreaterThanOrEqual(Money other) {
    return price.compareTo(other.wons()) >= 0;
  }
//
//    public BigDecimal getAmount() {
//        return amount;
//    }
//
//    public Long longValue() {
//        return amount.longValue();
//    }
//
//    public Double doubleValue() {
//        return amount.doubleValue();
//    }
//
//    public boolean equals(Object object) {
//        if (this == object) {
//            return true;
//        }
//
//        if (!(object instanceof Money)) {
//            return false;
//        }
//
//        Money other = (Money) object;
//        return Objects.equals(amount.doubleValue(), other.amount.doubleValue());
//    }
//
//    public int hashCode() {
//        return Objects.hashCode(amount);
//    }
//
//    public String toString() {
//        return amount.toString() + "원";
//    }
}