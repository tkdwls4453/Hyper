package com.dev.hyper.category.response;

import lombok.Builder;
import java.util.Set;

@Builder
public record CategoryResponse(
        String name,
        String parent,
        Set<String> children
) {
}
