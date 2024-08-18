package com.dev.hyper.item;

import com.dev.hyper.auth.dto.UserPrincipal;
import com.dev.hyper.common.response.CustomResponse;
import com.dev.hyper.item.request.CreateItemRequest;
import com.dev.hyper.item.request.UpdateItemRequest;
import com.dev.hyper.item.response.ItemResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/products")
public class ItemController {

    private final ItemService itemService;

    @PostMapping("/{productId}/items")
    public CustomResponse createItem(
            @Valid @RequestBody CreateItemRequest request,
            @PathVariable(name = "productId") Long productId,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        itemService.createItem(request, productId, principal.getUsername());
        return CustomResponse.OK();
    }

    @GetMapping("/{productId}/items")
    public CustomResponse<List<ItemResponse>> getItems(
            @PathVariable(name = "productId") Long productId,
            @AuthenticationPrincipal UserPrincipal principal
    ) {

        return CustomResponse.OK(
                itemService.getItems(productId, principal.getUsername())
        );
    }

    @PatchMapping("/items/{itemId}")
    public CustomResponse updateItem(
            @PathVariable(name = "itemId") Long itemId,
            @RequestBody UpdateItemRequest request,
            @AuthenticationPrincipal UserPrincipal principal
    ) {

        itemService.updateInfo(itemId, request, principal.getUsername());
        return CustomResponse.OK();
    }

    @DeleteMapping("/items/{itemId}")
    public CustomResponse deleteItem(
            @PathVariable(name = "itemId") Long itemId,
            @AuthenticationPrincipal UserPrincipal principal
    ){
        itemService.deleteItem(itemId, principal.getUsername());
        return CustomResponse.OK();
    }
}
