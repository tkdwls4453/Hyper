package com.dev.hyper.item.response;

import com.dev.hyper.item.Item;
import lombok.Builder;

@Builder
public record ItemResponse(
        Long id,
        String color,
        String size,
        int stock
) {


    public static ItemResponse from(Item item) {
        return ItemResponse.builder()
                .id(item.getId())
                .color(item.getColor())
                .size(item.getSize())
                .stock(item.getStock())
                .build();
    }
}
