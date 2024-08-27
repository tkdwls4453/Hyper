package com.dev.hyper.category.controller;

import com.dev.hyper.category.CategoryService;
import com.dev.hyper.category.response.CategoryResponse;
import com.dev.hyper.common.response.CustomResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "인증이 필요없는 카테고리 관련 API ", description = "인증이 필요없는 카테고리 관련 기능을 제공합니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/open-api/categories")
public class CategoryOpenController {
    private final CategoryService categoryService;

    @Operation(
            summary = "카테고리 조회",
            description = "등록되어 있는 모든 카테고리를 조회합니다."
    )
    @GetMapping
    public CustomResponse<List<CategoryResponse>> getCategories(){
        return CustomResponse.OK(
                categoryService.getCategories()
        );
    }
}
