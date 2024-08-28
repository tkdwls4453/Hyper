package com.dev.hyper.category;

import com.dev.hyper.category.repository.CategoryRepository;
import com.dev.hyper.category.request.CreateCategoryRequest;
import com.dev.hyper.category.request.UpdateCategoryRequest;
import com.dev.hyper.category.response.CategoryResponse;
import com.dev.hyper.common.error.CustomErrorException;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("test")
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
    class createCategory {

        @Test
        @DisplayName("부모 카테고리로 존재하지 않는 카테고리를 설정하면 부모 카테고리를 null 로 설정한다.")
        void test1() {
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
            sut.createCategory(request);

            // Then
            Category result = categoryRepository.findByName("child").orElse(null);

            assertThat(result).isNotNull();
            assertThat(result.name).isEqualTo("child");
            assertThat(result.parent).isNull();
        }

        @Test
        @DisplayName("정상적으로 카테고리 생성한다.")
        void test1000() {
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

    @Nested
    @DisplayName("카테고리 수정 테스트")
    class updateCategory {

        @Test
        @DisplayName("존재하지 않는 카테고리를 수정시, 예외가 발생한다.")
        void test1() {
            // Given
            Category category = Category.builder()
                    .name("category")
                    .build();

            Category parent = Category.builder()
                    .name("parent")
                    .build();

            Category savedCategory = categoryRepository.save(category);

            UpdateCategoryRequest request = UpdateCategoryRequest.builder()
                    .name("update")
                    .parentName("parent")
                    .build();

            // When
            assertThatThrownBy(
                    () -> sut.updateCategory(request, savedCategory.getId() + 100)
            )
                    .isInstanceOf(CustomErrorException.class)
                    .hasMessage("존재하지 않는 카테고리 입니다.");
        }

        @Test
        @DisplayName("존재하지 카테고리로 부모를 수정시, 예외가 발생한다.")
        void test2() {
            // Given
            Category category = Category.builder()
                    .name("category")
                    .build();

            Category savedCategory = categoryRepository.save(category);

            UpdateCategoryRequest request = UpdateCategoryRequest.builder()
                    .name("update")
                    .parentName("parent")
                    .build();

            // When
            assertThatThrownBy(
                    () -> sut.updateCategory(request, savedCategory.getId())
            )
                    .isInstanceOf(CustomErrorException.class)
                    .hasMessage("존재하지 않는 카테고리 입니다.");
        }

        @Test
        @DisplayName("정상적으로 카테고리 수정한다..")
        void test1000() {
            // Given
            Category childCategory = Category.builder()
                    .name("child")
                    .build();

            Category parentCategory = Category.builder()
                    .name("parent")
                    .build();

            Category savedChildCategory = categoryRepository.save(childCategory);
            Category savedParentCategory = categoryRepository.save(parentCategory);


            UpdateCategoryRequest request = UpdateCategoryRequest.builder()
                    .name("update")
                    .parentName("parent")
                    .build();

            // When
            sut.updateCategory(request, savedChildCategory.getId());

            // Then
            Category result = categoryRepository.findByName("update").orElse(null);

            assertThat(result).isNotNull();
            assertThat(result.name).isEqualTo("update");
            assertThat(result.parent).isEqualTo(savedParentCategory);
        }
    }

    @Nested
    @DisplayName("카테고리 삭제 테스트")
    class deleteCategory {

        @Test
        @DisplayName("존재하지 않는 카테고리를 삭제시, 예외가 발생한다.")
        void test1() {
            // Given

            // Expected
            assertThatThrownBy(
                    () -> {
                        sut.deleteCategory(1L);
                    }
            )
                    .isInstanceOf(CustomErrorException.class)
                    .hasMessage("존재하지 않는 카테고리 입니다.");


        }

        @Test
        @DisplayName("카테고리를 정상적으로 삭제한다.")
        void test1000() {
            // Given
            Category category = Category.builder()
                    .name("category")
                    .build();

            Category savedCategory = categoryRepository.save(category);

            // When
            sut.deleteCategory(savedCategory.getId());

            // Then
            Category result = categoryRepository.findById(savedCategory.getId()).orElse(null);
            assertThat(result).isNull();
        }
    }

    @Nested
    @DisplayName("카테고리 조회 테스트")
    class getCategories{
        @Test
        @DisplayName("모든 카테고리를 조회한다.")
        void test1(){
            // Given
            Category parent = Category.builder()
                    .name("parent")
                    .build();

            Category category = Category.builder()
                    .name("category")
                    .parent(parent)
                    .build();

            Category child1 = Category.builder()
                    .name("child1")
                    .parent(category)
                    .build();

            Category child2 = Category.builder()
                    .name("child2")
                    .parent(category)
                    .build();

            categoryRepository.saveAll(List.of(parent, category, child1, child2));

            // When
            List<CategoryResponse> result = sut.getCategories();

            // Then
            assertThat(result).hasSize(4)
                    .extracting("name", "parent", "children")
                    .containsExactlyInAnyOrder(
                            Tuple.tuple("parent", null, new HashSet<>(List.of("category"))),
                            Tuple.tuple("category", "parent", new HashSet<>(List.of("child1", "child2"))),
                            Tuple.tuple("child1", "category", new HashSet<>()),
                            Tuple.tuple("child2", "category", new HashSet<>())
                    );
        }
    }
}