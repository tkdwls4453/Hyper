package com.dev.hyper.product;

import com.dev.hyper.common.error.CustomErrorException;
import com.dev.hyper.product.domain.Product;
import com.dev.hyper.product.request.CreateProductRequest;
import com.dev.hyper.store.domain.Store;
import com.dev.hyper.store.repository.StoreRepository;
import com.dev.hyper.user.domain.Role;
import com.dev.hyper.user.domain.User;
import com.dev.hyper.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(properties = "JWT_SECRET=lalkwfmawlifawnfoiawnfioawfnafkslgnaw")
class ProductServiceTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductService productService;

    @PersistenceContext
    private EntityManager entityManager;

    @AfterEach
    void tearDown() {
        productRepository.deleteAllInBatch();
        storeRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @Nested
    @DisplayName("제품 생성 테스트")
    class createProduct{

        @Test
        @Transactional
        @DisplayName("판매자가 아닌 유저가 제품을 생성하려고 하면 예외를 반환한다.")
        void test1(){
            // Given
            User user = User.builder()
                    .email("test@naver.com")
                    .role(Role.BUYER)
                    .password("test123!@")
                    .name("test")
                    .build();

            Store store = Store.builder()
                    .name("store name")
                    .description("store description")
                    .isAccepted(true)
                    .build();

            store.updateUser(user);

            userRepository.save(user);
            storeRepository.save(store);

            entityManager.flush();

            CreateProductRequest request = CreateProductRequest.builder()
                    .name("product name")
                    .description("product description")
                    .build();

            // Expected
            assertThatThrownBy(() -> {
                productService.createProduct(request, user.getEmail());
            })
                    .isInstanceOf(CustomErrorException.class)
                    .hasMessage("제품 생성 권한이 없습니다.");

        }

        @Test
        @Transactional
        @DisplayName("제품을 생성하여 저장한다.")
        void test1000(){
            // Given
            User user = User.builder()
                    .email("test@naver.com")
                    .role(Role.SELLER)
                    .password("test123!@")
                    .name("test")
                    .build();

            Store store = Store.builder()
                    .name("store name")
                    .description("store description")
                    .isAccepted(true)
                    .build();

            store.updateUser(user);

            userRepository.save(user);
            storeRepository.save(store);

            entityManager.flush();

            CreateProductRequest request = CreateProductRequest.builder()
                    .name("product name")
                    .description("product description")
                    .build();

            // When
            productService.createProduct(request, user.getEmail());

            // Then
            List<Product> result = productRepository.findAll();
            assertThat(result).hasSize(1)
                    .extracting("name", "description")
                    .containsExactlyInAnyOrder(
                            Tuple.tuple("product name", "product description")
                    );

            Product product = result.get(0);

            assertThat(product.getUser()).isEqualTo(user);
            assertThat(product.getStore()).isEqualTo(store);
        }
    }

}