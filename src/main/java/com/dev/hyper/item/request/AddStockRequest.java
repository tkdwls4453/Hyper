package com.dev.hyper.item.request;

import lombok.Builder;

@Builder
public record AddStockRequest(
        int quantity
) {
}
