package com.dev.hyper.category;

import com.dev.hyper.category.repository.CategoryRepository;
import com.dev.hyper.category.repository.CategorySelfJoinResult;
import com.dev.hyper.category.request.CreateCategoryRequest;
import com.dev.hyper.category.request.UpdateCategoryRequest;
import com.dev.hyper.category.response.CategoryResponse;
import com.dev.hyper.common.error.CustomErrorException;
import com.dev.hyper.common.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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

    public void updateCategory(UpdateCategoryRequest request, Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> {
                    throw new CustomErrorException(ErrorCode.CATEGORY_NOT_FOUND_ERROR);
                }
        );

        if (request.name() != null) {
            category.updateName(request.name());
        }

        if (request.parentName() != null) {
            Category parent = categoryRepository.findByName(request.parentName()).orElseThrow(
                    () -> {
                        throw new CustomErrorException(ErrorCode.CATEGORY_NOT_FOUND_ERROR);
                    }
            );
            category.updateParent(parent);
        }

    }

    public void deleteCategory(Long categoryId) {
        if(categoryRepository.existsById(categoryId)){
            categoryRepository.deleteById(categoryId);
        }else{
            throw new CustomErrorException(ErrorCode.CATEGORY_NOT_FOUND_ERROR);
        }

    }

    @Transactional(readOnly = true)
    public List<CategoryResponse> getCategories() {
        List<CategorySelfJoinResult> resultList = categoryRepository.findAllSelfJoin();

        Map<String, Set<String>> childrenMap = new HashMap<>();
        Map<String, String> parentMap = new HashMap<>();

        for (CategorySelfJoinResult result : resultList) {
            childrenMap.putIfAbsent(result.getName(), new HashSet<>());
            parentMap.putIfAbsent(result.getName(), result.getParentName());

            if(result.getChildName() != null){
                childrenMap.get(result.getName()).add(result.getChildName());
            }

        }

        List<CategoryResponse> result = new ArrayList<>();

        for (String key : parentMap.keySet()) {
            result.add(CategoryResponse.builder()
                    .name(key)
                    .parent(parentMap.get(key))
                    .children(childrenMap.get(key))
                    .build()
            );
        }

        return result;
    }
}
