package com.dev.hyper.address.request;

import com.dev.hyper.address.AddressInfo;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record RegisterAddressRequest(
        @NotBlank(message = "주소의 제목은 필수입니다.")
        String title,

        @NotBlank(message = "주소는 필수입니다.")
        String address,
        String addressDetail,

        @NotBlank(message = "주소 코드는 필수입니다.")
        String code
) {
    public AddressInfo toEntity() {
        return AddressInfo.builder()
                .title(this.title)
                .address(this.address)
                .addressDetail(this.addressDetail)
                .code(this.code)
                .build();
    }
}
