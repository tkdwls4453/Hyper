package com.dev.hyper.product;

import com.dev.hyper.auth.dto.UserPrincipal;
import com.dev.hyper.common.response.CustomResponse;
import com.dev.hyper.product.request.CreateProductRequest;
import com.dev.hyper.product.request.UpdateProductRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public CustomResponse createProduct(
            @Valid @RequestBody CreateProductRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        productService.createProduct(request, userPrincipal.getUsername());
        return CustomResponse.OK();
    }

    @PatchMapping("/{id}")
    public CustomResponse updateProduct(
            @Valid @RequestBody UpdateProductRequest request,
            @PathVariable(name = "id") Long productId,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        productService.updateProduct(request, productId, userPrincipal.getUsername());
        return CustomResponse.OK();
    }

    @DeleteMapping("/{id}")
    public CustomResponse deleteProduct(
            @PathVariable(name = "id") Long productId,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        productService.deleteProduct(productId, userPrincipal.getUsername());
        return CustomResponse.OK();
    }

}
