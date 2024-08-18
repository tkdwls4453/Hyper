package com.dev.hyper.category.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record CreateCategoryRequest (
        @NotBlank(message = "카테고리 이름은 필수입니다.")
        String name,
        String parentName
) {
}
