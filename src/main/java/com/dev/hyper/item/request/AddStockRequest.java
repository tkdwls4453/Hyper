package com.dev.hyper.item.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record AddStockRequest(
        int quantity
) {
}
