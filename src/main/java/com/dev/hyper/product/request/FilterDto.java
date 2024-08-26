package com.dev.hyper.product.request;

import lombok.Builder;

import java.util.List;

@Builder
public record FilterDto(
        String category,
        List<Integer> priceRange
) {
}
