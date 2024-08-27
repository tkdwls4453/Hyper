package com.dev.hyper.product.controller;

import com.dev.hyper.auth.dto.UserPrincipal;
import com.dev.hyper.common.response.CustomResponse;
import com.dev.hyper.product.ProductService;
import com.dev.hyper.product.request.CreateProductRequest;
import com.dev.hyper.product.request.UpdateProductRequest;
import com.dev.hyper.product.response.ProductResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "인증이 필요한 제품 관련 API  (판매자용)", description = "판매자의 제품 관리를 이한 기능을 제공합니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @Operation(
            summary = "제품 생성",
            description = "제품을 생성합니다."
    )
    @PostMapping
    public CustomResponse createProduct(
            @Valid @RequestBody CreateProductRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        productService.createProduct(request, userPrincipal.getUsername());
        return CustomResponse.OK();
    }

    @Operation(
            summary = "제품 수정",
            description = "제품 정보를 수정합니다."
    )
    @PatchMapping("/{id}")
    public CustomResponse updateProduct(
            @Valid @RequestBody UpdateProductRequest request,
            @PathVariable(name = "id") Long productId,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        productService.updateProduct(request, productId, userPrincipal.getUsername());
        return CustomResponse.OK();
    }

    @Operation(
            summary = "제품 삭제",
            description = "제품을 삭제합니다."
    )
    @DeleteMapping("/{id}")
    public CustomResponse deleteProduct(
            @PathVariable(name = "id") Long productId,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        productService.deleteProduct(productId, userPrincipal.getUsername());
        return CustomResponse.OK();
    }

    @Operation(
            summary = "제품 조회",
            description = "판매자가 등록한 제품을 조회합니다.."
    )
    @GetMapping
    public CustomResponse<Page<ProductResponse>> getProducts(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(name = "sort") String sortingCondition,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {

        return CustomResponse.OK(
                productService.getProducts(pageable, sortingCondition, userPrincipal.getUsername())
        );
    }

}
