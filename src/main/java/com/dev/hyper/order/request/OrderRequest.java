package com.dev.hyper.order.request;

import lombok.Builder;

import java.util.List;

@Builder
public record OrderRequest(
        Long addressId,
        List<OrderItemInfo> OrderItems
) {
}
