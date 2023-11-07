package com.windmeal.generic.domain;

import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Function;
import lombok.Getter;


@Embeddable
@Getter
public class Money {
    public static final Money ZERO = Money.wons(0);

    private int price;

    private Money(int price) {
        this.price = price;
    }

    public Money() {

    }
    public int wons(){
        return this.price;
    }
    public static Money wons(int amount) {
        return new Money(amount);
    }
//
//    public static Money wons(double amount) {
//        return new Money(BigDecimal.valueOf(amount));
//    }
//
//    public static <T> Money sum(Collection<T> bags, Function<T, Money> monetary) {
//        return bags.stream().map(bag -> monetary.apply(bag)).reduce(Money.ZERO, Money::plus);
//    }
//
//    Money(BigDecimal amount) {
//        this.amount = amount;
//    }
//
//    public Money plus(Money amount) {
//        return new Money(this.amount.add(amount.amount));
//    }
//
//    public Money minus(Money amount) {
//        return new Money(this.amount.subtract(amount.amount));
//    }
//
//    public Money times(double percent) {
//        return new Money(this.amount.multiply(BigDecimal.valueOf(percent)));
//    }
//
//    public Money divide(double divisor) {
//        return new Money(amount.divide(BigDecimal.valueOf(divisor)));
//    }
//
//    public boolean isLessThan(Money other) {
//        return amount.compareTo(other.amount) < 0;
//    }
//
//    public boolean isGreaterThanOrEqual(Money other) {
//        return amount.compareTo(other.amount) >= 0;
//    }
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