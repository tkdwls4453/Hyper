package com.dev.hyper.order.controller;

import com.dev.hyper.auth.dto.UserPrincipal;
import com.dev.hyper.common.response.CustomResponse;
import com.dev.hyper.order.request.OrderRequest;
import com.dev.hyper.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public CustomResponse createOrder(
            @RequestBody OrderRequest orderRequest,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        orderService.createOrder(orderRequest, userPrincipal.getUsername());
        return CustomResponse.OK();
    }
}
