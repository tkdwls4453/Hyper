package com.dev.hyper.category;

import com.dev.hyper.category.request.CreateCategoryRequest;
import com.dev.hyper.common.error.CustomErrorException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@Transactional
@SpringBootTest(properties = "JWT_SECRET=lalkwfmawlifawnfoiawnfioawfnafkslgnaw")
class CategoryServiceTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryService sut;

    @AfterEach
    void tearDown() {
        categoryRepository.deleteAllInBatch();
    }

    @Nested
    @DisplayName("카테고리 생성 테스트")
    class createCategory{

        @Test
        @DisplayName("부모 카테고리로 존재하지 않는 카테고리를 설정하면, 예외가 발생한다.")
        void test1(){
            // Given
            Category parentCategory = Category.builder()
                    .name("parent")
                    .build();

            Category savedParentCategory = categoryRepository.save(parentCategory);

            CreateCategoryRequest request = CreateCategoryRequest.builder()
                    .name("child")
                    .parentName("NO")
                    .build();

            // When
            assertThatThrownBy(
                    () -> sut.createCategory(request)
            )
                    .isInstanceOf(CustomErrorException.class)
                    .hasMessage("존재하지 않는 카테고리 입니다.");
        }

        @Test
        @DisplayName("정상적으로 카테고리 생성한다.")
        void test1000(){
            // Given
            Category parentCategory = Category.builder()
                    .name("parent")
                    .build();

            Category savedParentCategory = categoryRepository.save(parentCategory);

            CreateCategoryRequest request = CreateCategoryRequest.builder()
                    .name("child")
                    .parentName("parent")
                    .build();

            // When
            sut.createCategory(request);

            // Then
            Category result = categoryRepository.findByName("child").orElse(null);

            assertThat(result).isNotNull();
            assertThat(result.name).isEqualTo("child");
            assertThat(result.parent).isEqualTo(savedParentCategory);
        }
    }
}