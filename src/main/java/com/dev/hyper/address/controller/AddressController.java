package com.dev.hyper.address.controller;

import com.dev.hyper.address.AddressService;
import com.dev.hyper.address.request.RegisterAddressRequest;
import com.dev.hyper.auth.dto.UserPrincipal;
import com.dev.hyper.common.response.CustomResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/address")
public class AddressController {

    private final AddressService addressService;

    @PostMapping
    public CustomResponse registerAddress(
            @Valid @RequestBody RegisterAddressRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {

        addressService.registerAddress(request, userPrincipal.getUsername());
        return CustomResponse.OK();
    }
}
