package com.dev.hyper.order.controller;

import com.dev.hyper.auth.dto.UserPrincipal;
import com.dev.hyper.common.response.CustomResponse;
import com.dev.hyper.order.request.OrderRequest;
import com.dev.hyper.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "주문 관련 API ", description = "구매자의 주문 관련 기능을 제공합니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @Operation(
            summary = "주문 생성",
            description = "구매자가 주문하면, 재고가 감소하고 주문을 생성합니다."
    )
    @PostMapping
    public CustomResponse createOrder(
            @RequestBody OrderRequest orderRequest,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        orderService.createOrder(orderRequest, userPrincipal.getUsername());
        return CustomResponse.OK();
    }
}
