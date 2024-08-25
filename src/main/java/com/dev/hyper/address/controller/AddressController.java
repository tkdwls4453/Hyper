package com.dev.hyper.address.controller;

import com.dev.hyper.address.AddressService;
import com.dev.hyper.address.request.RegisterAddressRequest;
import com.dev.hyper.address.response.AddressResponse;
import com.dev.hyper.auth.dto.UserPrincipal;
import com.dev.hyper.common.response.CustomResponse;
import com.dev.hyper.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/addresses")
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

    @GetMapping
    public CustomResponse<List<AddressResponse>> getAllAddresses(
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return CustomResponse.OK(
                addressService.getAllAddresses(userPrincipal.getUsername())
        );
    }

}
