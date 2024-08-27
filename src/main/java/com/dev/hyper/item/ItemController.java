package com.dev.hyper.item;

import com.dev.hyper.auth.dto.UserPrincipal;
import com.dev.hyper.common.response.CustomResponse;
import com.dev.hyper.item.request.AddStockRequest;
import com.dev.hyper.item.request.CreateItemRequest;
import com.dev.hyper.item.request.ReduceStockRequest;
import com.dev.hyper.item.request.UpdateItemRequest;
import com.dev.hyper.item.response.ItemResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "아이템 관련 API (판매자용)", description = "판매자의 아이템 등록 및 관리 기능을 제공합니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/products")
public class ItemController {

    private final ItemService itemService;

    @Operation(
            summary = "아이템 생성",
            description = "아이템을 생성합니다."
    )
    @PostMapping("/{productId}/items")
    public CustomResponse createItem(
            @Valid @RequestBody CreateItemRequest request,
            @PathVariable(name = "productId") Long productId,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        itemService.createItem(request, productId, principal.getUsername());
        return CustomResponse.OK();
    }

    @Operation(
            summary = "특정 제품의 아이템 조회",
            description = "특정 제품에 해당되는 모든 아이템 정보를 조회합니다."
    )
    @GetMapping("/{productId}/items")
    public CustomResponse<List<ItemResponse>> getItems(
            @PathVariable(name = "productId") Long productId,
            @AuthenticationPrincipal UserPrincipal principal
    ) {

        return CustomResponse.OK(
                itemService.getItems(productId, principal.getUsername())
        );
    }

    @Operation(
            summary = "아이템 수정",
            description = "아이템 정보를 수정합니다."
    )
    @PatchMapping("/items/{itemId}")
    public CustomResponse updateItem(
            @PathVariable(name = "itemId") Long itemId,
            @RequestBody UpdateItemRequest request,
            @AuthenticationPrincipal UserPrincipal principal
    ) {

        itemService.updateInfo(itemId, request, principal.getUsername());
        return CustomResponse.OK();
    }

    @Operation(
            summary = "아이템 삭제",
            description = "아이템을 제거합니다."
    )
    @DeleteMapping("/items/{itemId}")
    public CustomResponse deleteItem(
            @PathVariable(name = "itemId") Long itemId,
            @AuthenticationPrincipal UserPrincipal principal
    ){
        itemService.deleteItem(itemId, principal.getUsername());
        return CustomResponse.OK();
    }

    @Operation(
            summary = "아이템 재고 추가",
            description = "특정 아이템의 재고를 추가합니다."
    )
    @PatchMapping("/items/{itemId}/stock/add")
    public CustomResponse addStock(
            @PathVariable(name = "itemId") Long itemId,
            @RequestBody AddStockRequest request,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        itemService.addStock(itemId, request, principal.getUsername());
        return CustomResponse.OK();
    }

    @Operation(
            summary = "아이템 재고 감소",
            description = "특정 아이템의 재고를 차감합니다."
    )
    @PatchMapping("/items/{itemId}/stock/reduce")
    public CustomResponse reduceStock(
            @PathVariable(name = "itemId") Long itemId,
            @Valid @RequestBody ReduceStockRequest request,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        itemService.reduceStock(itemId, request, principal.getUsername());
        return CustomResponse.OK();
    }
}
