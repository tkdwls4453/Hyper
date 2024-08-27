package com.dev.hyper.order.request;

import lombok.Builder;

@Builder
public record OrderItemInfo(
        Long itemId,
        int quantity
) {
}
