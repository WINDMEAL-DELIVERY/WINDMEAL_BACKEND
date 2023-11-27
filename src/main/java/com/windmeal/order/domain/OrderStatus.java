package com.windmeal.order.domain;

public enum OrderStatus {
     //주문 전, 배달 중, 배달완료, 취소
     ORDERED("배달 매칭 전"),
     DELIVERING("배달 중"),
     DELIVERED("배달 완료"),
     CANCELED("취소");

     private final String status;

     OrderStatus(String status) {
          this.status = status;
     }

     public String getStatus() {
          return this.status;
     }
}
