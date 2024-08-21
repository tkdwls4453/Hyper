package com.dev.hyper.product.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UpdateProductRequest(
    @NotBlank(message = "제품 이름은 필수입니다.")
    String name,
    String description,
    String category
){

}
