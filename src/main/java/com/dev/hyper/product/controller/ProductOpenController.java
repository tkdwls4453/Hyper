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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


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
            @Parameter(description = "검색어") @RequestParam("q") String search,
            @Parameter(description = "정렬 조건 [latest, oldest, cheap, expensive 중 1개 선택]") @RequestParam("sort") String sort,
            @Parameter(description = "필터링 조건 {category: OO, priceRange: [0000, 0000]}") @RequestParam("filter") String filterJson,
            @Parameter(description = "페이징 조건") @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable
            ){
        FilterDto filterDto;
        try {
            filterDto = filterJson != null ? objectMapper.readValue(filterJson, FilterDto.class) : null;
        } catch (JsonProcessingException e) {
            throw new CustomErrorException(ErrorCode.JSON_PARSING_ERROR);
        }

        return CustomResponse.OK(
                productService.searchProducts(search, sort, filterDto, pageable)
        );
    }
}
