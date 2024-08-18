package com.dev.hyper.item.request;

import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record ReduceStockRequest(

        @Positive(message = "재고 감소량은 양수만 가능합니다.")
        int quantity
) {
}
