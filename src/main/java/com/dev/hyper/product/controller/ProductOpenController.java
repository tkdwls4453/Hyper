package com.dev.hyper.product.controller;

import com.dev.hyper.common.error.CustomErrorException;
import com.dev.hyper.common.error.ErrorCode;
import com.dev.hyper.common.response.CustomResponse;
import com.dev.hyper.product.ProductService;
import com.dev.hyper.product.request.FilterDto;
import com.dev.hyper.product.response.ProductResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("open-api/products")
public class ProductOpenController {

    private final ProductService productService;
    private final ObjectMapper objectMapper;

    @GetMapping("/search")
    public CustomResponse<Page<ProductResponse>> searchProduct(
            @RequestParam("q") String search,
            @RequestParam("sort") String sort,
            @RequestParam("filter") String filterJson,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable
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
