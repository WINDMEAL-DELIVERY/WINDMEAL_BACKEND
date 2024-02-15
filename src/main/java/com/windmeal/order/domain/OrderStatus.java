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

     public static OrderStatus of(String source) {
          if (source == null) {
               return null;
          }
          switch (source.toUpperCase()) {
               case "ORDERED":
                    return OrderStatus.ORDERED;
               case "DELIVERING":
                    return OrderStatus.DELIVERING;
               case "DELIVERED":
                    return OrderStatus.DELIVERED;
               case "CANCELED":
                    return OrderStatus.CANCELED;
               default:
                    throw new IllegalArgumentException("일치하는 주문 상태가 존재하지 않습니다.");
          }
     }
}
