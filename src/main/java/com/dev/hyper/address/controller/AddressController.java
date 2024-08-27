package com.dev.hyper.address.controller;

import com.dev.hyper.address.AddressService;
import com.dev.hyper.address.request.RegisterAddressRequest;
import com.dev.hyper.address.response.AddressResponse;
import com.dev.hyper.auth.dto.UserPrincipal;
import com.dev.hyper.common.response.CustomResponse;
import com.dev.hyper.user.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "배송지 관련 API", description = "구매자의 배송지 등록 및 관리 기능을 제공합니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/addresses")
public class AddressController {

    private final AddressService addressService;

    @Operation(
            summary = "배송지 등록",
            description = "유저의 새로운 배송지를 등록합니다."
    )
    @PostMapping
    public CustomResponse registerAddress(
            @Valid @RequestBody RegisterAddressRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {

        addressService.registerAddress(request, userPrincipal.getUsername());
        return CustomResponse.OK();
    }

    @Operation(
            summary = "배송지 조회",
            description = "유저가 등록한 모든 배송지를 조회합니다."
    )
    @GetMapping
    public CustomResponse<List<AddressResponse>> getAllAddresses(
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return CustomResponse.OK(
                addressService.getAllAddresses(userPrincipal.getUsername())
        );
    }

}
