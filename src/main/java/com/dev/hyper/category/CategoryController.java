package com.dev.hyper.category;

import com.dev.hyper.category.request.CreateCategoryRequest;
import com.dev.hyper.common.response.CustomResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
