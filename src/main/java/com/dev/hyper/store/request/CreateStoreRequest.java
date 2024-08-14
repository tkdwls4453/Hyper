package com.dev.hyper.store.request;

import com.dev.hyper.store.domain.Store;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record CreateStoreRequest(
        @NotBlank(message = "스토어 이름은 필수 입니다.")
        String name,
        String description
) {
        public Store toEntity() {
                return Store.builder()
                        .name(this.name)
                        .description(this.description)
                        .isAccepted(false)
                        .build();
        }
}
