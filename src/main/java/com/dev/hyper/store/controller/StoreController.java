package com.dev.hyper.store.controller;

import com.dev.hyper.common.response.CustomResponse;
import com.dev.hyper.store.request.CreateStoreRequest;
import com.dev.hyper.store.service.StoreService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/store")
public class StoreController {

    private final StoreService storeService;

    @PostMapping
    public CustomResponse createStore(
            @Valid @RequestBody CreateStoreRequest request
    ) {
        storeService.createStore(request);
        return CustomResponse.OK();
    }
}
