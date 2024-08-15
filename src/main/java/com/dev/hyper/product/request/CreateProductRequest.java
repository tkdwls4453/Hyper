package com.dev.hyper.product.request;

import com.dev.hyper.product.domain.Product;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record CreateProductRequest(
        @NotBlank(message = "제품 이름은 필수입니다.")
        String name,
        String description
) {
        public Product toEntity() {
                return Product.builder()
                        .name(this.name)
                        .description(this.description)
                        .build();
        }

}
