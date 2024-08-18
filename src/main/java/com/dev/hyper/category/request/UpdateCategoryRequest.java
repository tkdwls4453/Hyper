package com.dev.hyper.category.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UpdateCategoryRequest(
        String name,

        String parentName
) {
}
