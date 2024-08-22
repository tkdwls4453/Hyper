package com.dev.hyper.product.response;

import com.dev.hyper.product.repository.dto.ProductQueryResult;
import lombok.Builder;

import java.time.format.DateTimeFormatter;

@Builder
public record ProductResponse(
        Long id,
        String name,
        String description,
        String category,
        String price,
        String createdDate,
        String lastModifiedDate
) {

    public static ProductResponse from(ProductQueryResult queryResult) {
        return ProductResponse.builder()
                .id(queryResult.getId())
                .name(queryResult.getName())
                .description(queryResult.getDescription())
                .category(queryResult.getCategory())
                .price(String.format("%,d", queryResult.getPrice()))
                .createdDate(queryResult.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")))
                .lastModifiedDate(queryResult.getLastModifiedDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")))
                .build();
    }

}
