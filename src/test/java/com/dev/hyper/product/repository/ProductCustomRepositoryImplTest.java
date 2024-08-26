package com.dev.hyper.product.repository;

import com.dev.hyper.category.Category;
import com.dev.hyper.category.repository.CategoryRepository;
import com.dev.hyper.common.config.JpaAuditingConfig;
import com.dev.hyper.product.domain.Product;
import com.dev.hyper.product.repository.dto.ProductQueryResult;
import com.dev.hyper.product.request.FilterDto;
import com.dev.hyper.user.domain.Role;
import com.dev.hyper.user.domain.User;
import com.dev.hyper.user.repository.UserRepository;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@Import(JpaAuditingConfig.class)
@DataJpaTest
class ProductCustomRepositoryImplTest {

    @Autowired
    private ProductRepository sut;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        User user = User.builder()
                .name("user")
                .role(Role.SELLER)
                .email("test@naver.com")
                .password("thisistest412!@")
                .build();

        Category category = Category.builder()
                .name("category")
                .build();

        Category category2 = Category.builder()
                .name("category2")
                .build();

        userRepository.save(user);
        categoryRepository.saveAll(List.of(category, category2));

        for (int i = 1; i <= 100; i++) {
            sut.save(Product.builder()
                    .name("product" + i)
                    .description("description")
                    .price(i * 1000)
                    .user(user)
                    .category(category)
                    .build());
        }

        for (int i = 1; i <= 10; i++) {
            sut.save(Product.builder()
                    .name("product-" + i)
                    .description("description")
                    .price(i * 1000)
                    .user(user)
                    .category(category2)
                    .build());
        }
    }

    @AfterEach
    void tearDown() {
        sut.deleteAllInBatch();
        categoryRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @Nested
    @DisplayName("제품을 정렬하여 조회한다.")
    class findAllByUserEmailWithSorting{

        @Test
        @DisplayName("최신순으로 제품을 조회한다.")
        void test1(){
            // Given
            Pageable pageable = PageRequest.of(1, 3);
            String sortingCondition = "latest";
            String email = "test@naver.com";

            // When
            Page<ProductQueryResult> result = sut.findAllByUserEmailWithSorting(pageable, sortingCondition, email);

            // Then
            List<ProductQueryResult> content = result.getContent();
            long total = result.getTotalElements();

            assertThat(content).hasSize(3)
                    .extracting( "name", "price")
                    .containsExactly(
                            Tuple.tuple("product4", 4000),
                            Tuple.tuple("product5", 5000),
                            Tuple.tuple("product6", 6000)
                    );

            assertThat(total).isEqualTo(110);
        }

        @Test
        @DisplayName("오래된 순으로 제품을 조회한다.")
        void test2(){
            // Given
            Pageable pageable = PageRequest.of(1, 3);
            String sortingCondition = "oldest";
            String email = "test@naver.com";

            // When
            Page<ProductQueryResult> result = sut.findAllByUserEmailWithSorting(pageable, sortingCondition, email);

            // Then
            List<ProductQueryResult> content = result.getContent();
            long total = result.getTotalElements();

            assertThat(content).hasSize(3)
                    .extracting( "name", "price")
                    .containsExactly(
                            Tuple.tuple("product-7", 7000),
                            Tuple.tuple("product-6", 6000),
                            Tuple.tuple("product-5", 5000)
                    );

            assertThat(total).isEqualTo(110);
        }

        @Test
        @DisplayName("가격이 낮은순으로 제품을 조회한다.")
        void test3(){
            // Given
            Pageable pageable = PageRequest.of(1, 3);
            String sortingCondition = "cheap";
            String email = "test@naver.com";

            // When
            Page<ProductQueryResult> result = sut.findAllByUserEmailWithSorting(pageable, sortingCondition, email);

            // Then
            List<ProductQueryResult> content = result.getContent();
            long total = result.getTotalElements();

            assertThat(content).hasSize(3)
                    .extracting("name", "price")
                    .containsExactly(
                            Tuple.tuple("product-2", 2000),
                            Tuple.tuple("product3", 3000),
                            Tuple.tuple("product-3", 3000)
                    );

            assertThat(total).isEqualTo(110);
        }

        @Test
        @DisplayName("가격이 높은 순으로 제품을 조회한다.")
        void test4(){
            // Given
            Pageable pageable = PageRequest.of(1, 3);
            String sortingCondition = "expensive";
            String email = "test@naver.com";

            // When
            Page<ProductQueryResult> result = sut.findAllByUserEmailWithSorting(pageable, sortingCondition, email);

            // Then
            List<ProductQueryResult> content = result.getContent();
            long total = result.getTotalElements();

            assertThat(content).hasSize(3)
                    .extracting("name", "price")
                    .containsExactly(
                            Tuple.tuple("product97", 97000),
                            Tuple.tuple( "product96", 96000),
                            Tuple.tuple("product95", 95000)
                    );

            assertThat(total).isEqualTo(110);
        }
    }

    @Nested
    @DisplayName("제품 검색 및 필터링 테스트")
    class searchProduct{
        @Test
        @DisplayName("검색어를 포함한 제픔들을 검색한다.")
        void test1(){
            // Given
            String search = "2";
            String sortingCondition = "latest";
            Pageable pageable = PageRequest.of(0, 3);

            // When
            Page<ProductQueryResult> result = sut.search(search, sortingCondition, null, pageable);

            // Then
            assertThat(result).hasSize(3)
                    .extracting("name")
                    .containsExactly("product2", "product12", "product20");
        }

        @Test
        @DisplayName("필터링을 포함한 제픔들을 검색한다.")
        void test2(){
            // Given


            String sortingCondition = "latest";
            Pageable pageable = PageRequest.of(2, 3);

            FilterDto filterDto = FilterDto.builder()
                    .category("category2")
                    .priceRange(List.of(1000, 8000))
                    .build();

            // When
            Page<ProductQueryResult> result = sut.search(null, sortingCondition, filterDto, pageable);

            // Then
            assertThat(result).hasSize(2)
                    .extracting("name", "category", "price")
                    .containsExactly(
                            Tuple.tuple("product-7", "category2", 7000),
                            Tuple.tuple("product-8", "category2", 8000)
                    );
        }

        @Test
        @DisplayName("정렬 조건을 포함한 제픔들을 검색한다.")
        void test3(){
            // Given


            String sortingCondition = "expensive";
            Pageable pageable = PageRequest.of(0, 3);

            FilterDto filterDto = FilterDto.builder()
                    .priceRange(List.of(1000, 8000))
                    .build();

            // When
            Page<ProductQueryResult> result = sut.search(null, sortingCondition, filterDto, pageable);

            // Then
            assertThat(result).hasSize(3)
                    .extracting("name", "category", "price")
                    .containsExactly(
                            Tuple.tuple("product8", "category", 8000),
                            Tuple.tuple("product-8", "category2", 8000),
                            Tuple.tuple("product7", "category", 7000)
                    );
        }
    }
}