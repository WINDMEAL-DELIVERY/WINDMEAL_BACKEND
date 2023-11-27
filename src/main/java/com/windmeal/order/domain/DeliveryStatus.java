package com.windmeal.order.domain;

public enum DeliveryStatus {
    DELIVERING("배송 중"),
    DELIVERED("배송 완료"),
    CANCELED("취소됨");

    private final String status;

    DeliveryStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }
}
