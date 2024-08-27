package com.dev.hyper.store.controller;

import com.dev.hyper.auth.dto.UserPrincipal;
import com.dev.hyper.common.response.CustomResponse;
import com.dev.hyper.store.request.CreateStoreRequest;
import com.dev.hyper.store.service.StoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "스토어 관련 API", description = "판매자의 스토어 등록 및 관리 기능을 제공합니다.")
@AllArgsConstructor
@RestController
@RequestMapping("/api/stores")
public class StoreController {

    private final StoreService storeService;

    @Operation(
            summary = "스토어 등록",
            description = "새로운 스토어를 등록합니다."
    )
    @PostMapping
    public CustomResponse createStore(
            @Valid @RequestBody CreateStoreRequest request,
            @AuthenticationPrincipal UserPrincipal principal
            ) {
        storeService.createStore(request, principal.getUsername());
        return CustomResponse.OK();
    }
}
