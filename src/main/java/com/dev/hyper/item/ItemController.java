package com.dev.hyper.item;

import com.dev.hyper.auth.dto.UserPrincipal;
import com.dev.hyper.common.response.CustomResponse;
import com.dev.hyper.item.request.CreateItemRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/products")
public class ItemController {

    private final ItemService itemService;

    @PostMapping("/{productId}/items")
    public CustomResponse createItem(
            @Valid @RequestBody CreateItemRequest request,
            @PathVariable(name = "productId") Long id,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        itemService.createItem(request, id, principal.getUsername());
        return CustomResponse.OK();
    }
}
