package com.dev.hyper.address.response;
import com.dev.hyper.address.AddressInfo;
import lombok.Builder;

@Builder
public record AddressResponse(
        Long id,
        String title,
        String address,
        String addressDetail,
        String code
){

    public static AddressResponse from(AddressInfo addressInfo) {
        return AddressResponse.builder()
                .id(addressInfo.getId())
                .title(addressInfo.getTitle())
                .address(addressInfo.getAddress())
                .addressDetail(addressInfo.getAddressDetail())
                .code(addressInfo.getCode())
                .build();
    }
}
