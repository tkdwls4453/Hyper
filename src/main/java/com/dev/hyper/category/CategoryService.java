package com.dev.hyper.category;

import com.dev.hyper.category.request.CreateCategoryRequest;
import com.dev.hyper.common.error.CustomErrorException;
import com.dev.hyper.common.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    public void createCategory(CreateCategoryRequest request) {

        Category parentCategory = categoryRepository.findByName(request.parentName()).orElseThrow(
                () -> {
                    throw new CustomErrorException(ErrorCode.CATEGORY_NOT_FOUND_ERROR);
                }
        );

        Category category = Category.builder()
                .name(request.name())
                .build();

        category.updateParent(parentCategory);

        categoryRepository.save(category);
    }
}
