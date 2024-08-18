package com.dev.hyper.item.request;

import lombok.Builder;

@Builder
public record UpdateItemRequest(
        String color,
        String size,
        int stock
) {
}
