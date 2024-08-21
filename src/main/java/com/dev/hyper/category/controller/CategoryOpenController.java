package com.dev.hyper.category.controller;

import com.dev.hyper.category.CategoryService;
import com.dev.hyper.category.response.CategoryResponse;
import com.dev.hyper.common.response.CustomResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/open-api/categories")
public class CategoryOpenController {
    private final CategoryService categoryService;

    @GetMapping
    public CustomResponse<List<CategoryResponse>> getCategories(){
        return CustomResponse.OK(
                categoryService.getCategories()
        );
    }
}
