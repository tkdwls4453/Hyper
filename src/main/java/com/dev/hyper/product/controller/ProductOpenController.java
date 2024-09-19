package com.dev.hyper.product.controller;

import com.dev.hyper.common.error.CustomErrorException;
import com.dev.hyper.common.error.ErrorCode;
import com.dev.hyper.common.response.CustomResponse;
import com.dev.hyper.product.ProductService;
import com.dev.hyper.product.request.FilterDto;
import com.dev.hyper.product.response.ProductResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;


@Tag(name = "인증이 필요없는 제품 관련 API", description = "제품 검색 같은 인증이 필요없는 제품 관련 기능을 제공합니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("open-api/products")
public class ProductOpenController {

    private final ProductService productService;
    private final ObjectMapper objectMapper;

    @Operation(
            summary = "제품 검색",
            description = "검색어, 필터 조건, 정렬 조건을 사용하여 제품을 조회합니다."
    )
    @GetMapping("/search")
    public CustomResponse<Page<ProductResponse>> searchProduct(
            @Parameter(description = "검색어") @RequestParam(value = "q", required = false) String search,
            @Parameter(description = "정렬 조건 [latest, oldest, cheap, expensive 중 1개 선택]") @RequestParam(value = "sort", required = false) String sort,
            @RequestBody(required = false) FilterDto filterDto,
            @Parameter(description = "페이징 조건") @PageableDefault(size = 10) Pageable pageable
            ){

        return CustomResponse.OK(
                productService.searchProducts(search, sort, filterDto, pageable)
        );
    }
}
