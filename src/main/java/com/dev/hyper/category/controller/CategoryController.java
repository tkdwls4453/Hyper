package com.dev.hyper.category.controller;

import com.dev.hyper.category.CategoryService;
import com.dev.hyper.category.request.CreateCategoryRequest;
import com.dev.hyper.category.request.UpdateCategoryRequest;
import com.dev.hyper.category.response.CategoryResponse;
import com.dev.hyper.common.response.CustomResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "카테고리 관련 API (관리자용)", description = "관리자의 카테고리 관리 기능을 제공합니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(
            summary = "새로운 카테고리 등록",
            description = "관리자가 새로운 카테고리를 생성합니다."
    )
    @PostMapping
    public CustomResponse createCategory(
            @Valid @RequestBody CreateCategoryRequest request
    ){
        categoryService.createCategory(request);
        return CustomResponse.OK();
    }

    @Operation(
            summary = "카테고리 수정",
            description = "카테고리의 내용을 수정합니다."
    )
    @PatchMapping("/{categoryId}")
    public CustomResponse updateCategory(
            @Valid @RequestBody UpdateCategoryRequest request,
            @PathVariable(name = "categoryId") Long categoryId
    ){
        categoryService.updateCategory(request, categoryId);
        return CustomResponse.OK();
    }

    @Operation(
            summary = "카테고리 삭제",
            description = "카테고리를 삭제합니다."
    )
    @DeleteMapping("/{categoryId}")
    public CustomResponse deleteCategory(
            @PathVariable(name = "categoryId") Long categoryId
    ){
        categoryService.deleteCategory(categoryId);
        return CustomResponse.OK();
    }
}
