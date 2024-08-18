package com.dev.hyper.item.request;

import com.dev.hyper.item.Item;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import org.springframework.util.StringUtils;

import static org.springframework.util.StringUtils.*;

@Builder
public record CreateItemRequest(
        @NotBlank(message = "색상 정보는 필수입니다.")
        String color,
        String size,
        int stock
) {


    public Item toEntity() {
        return Item.builder()
                .color(color)
                .size(hasText(size) ? size : "free")
                .stock(stock)
                .build();
    }
}
