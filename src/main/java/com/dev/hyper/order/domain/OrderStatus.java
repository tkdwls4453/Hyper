package com.dev.hyper.order.domain;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum OrderStatus {
    PAYMENT_COMPLETED("결제"),
    PREPARING("준비중"),
    SHIPPED("배송중"),
    DELIVERED("배송완료"),
    PURCHASE_CONFIRMED("구매확정");

    private String message;
}
