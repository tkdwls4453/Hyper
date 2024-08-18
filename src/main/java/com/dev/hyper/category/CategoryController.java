package com.dev.hyper.category;

import com.dev.hyper.category.request.CreateCategoryRequest;
import com.dev.hyper.category.request.UpdateCategoryRequest;
import com.dev.hyper.common.response.CustomResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public CustomResponse createCategory(
            @Valid @RequestBody CreateCategoryRequest request
    ){
        categoryService.createCategory(request);
        return CustomResponse.OK();
    }

    @PatchMapping("/{categoryId}")
    public CustomResponse updateCategory(
            @Valid @RequestBody UpdateCategoryRequest request,
            @PathVariable(name = "categoryId") Long categoryId
    ){
        categoryService.updateCategory(request, categoryId);
        return CustomResponse.OK();
    }

    @DeleteMapping("/{categoryId}")
    public CustomResponse deleteCategory(
            @PathVariable(name = "categoryId") Long categoryId
    ){
        categoryService.deleteCategory(categoryId);
        return CustomResponse.OK();
    }
}
