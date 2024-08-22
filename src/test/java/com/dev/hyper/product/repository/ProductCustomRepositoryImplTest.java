package com.dev.hyper.product.repository;

import com.dev.hyper.category.Category;
import com.dev.hyper.category.repository.CategoryRepository;
import com.dev.hyper.common.config.JpaAuditingConfig;
import com.dev.hyper.product.domain.Product;
import com.dev.hyper.product.repository.dto.ProductQueryResult;
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

        userRepository.save(user);
        categoryRepository.save(category);

        for (int i = 1; i <= 100; i++) {
            sut.save(Product.builder()
                    .name("product" + i)
                    .description("description")
                    .price(i * 1000)
                    .user(user)
                    .category(category)
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

            assertThat(total).isEqualTo(100);
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
                            Tuple.tuple("product97", 97000),
                            Tuple.tuple("product96", 96000),
                            Tuple.tuple("product95", 95000)
                    );

            assertThat(total).isEqualTo(100);
        }

        @Test
        @DisplayName("가격이 낮은순으로 제품을 조회한다.")
        void test3(){
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
                    .extracting("name", "price")
                    .containsExactly(
                            Tuple.tuple("product4", 4000),
                            Tuple.tuple("product5", 5000),
                            Tuple.tuple("product6", 6000)
                    );

            assertThat(total).isEqualTo(100);
        }

        @Test
        @DisplayName("가격이 높은 순으로 제품을 조회한다.")
        void test4(){
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
                    .extracting("name", "price")
                    .containsExactly(
                            Tuple.tuple("product97", 97000),
                            Tuple.tuple( "product96", 96000),
                            Tuple.tuple("product95", 95000)
                    );

            assertThat(total).isEqualTo(100);
        }
    }

}