package com.dev.hyper.product;

import com.dev.hyper.common.error.CustomErrorException;
import com.dev.hyper.product.domain.Product;
import com.dev.hyper.product.request.CreateProductRequest;
import com.dev.hyper.product.request.UpdateProductRequest;
import com.dev.hyper.store.domain.Store;
import com.dev.hyper.store.repository.StoreRepository;
import com.dev.hyper.user.domain.Role;
import com.dev.hyper.user.domain.User;
import com.dev.hyper.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@Transactional
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
        @DisplayName("판매자가 아닌 유저가 제품을 생성하려고 하면 예외를 반환한다.")
        void test1(){
            // Given
            User user = createUser(Role.BUYER);

            Store store = createStore();

            store.updateUser(user);

            userRepository.save(user);
            storeRepository.save(store);

            entityManager.flush();
            entityManager.clear();
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
        @DisplayName("제품을 생성하여 저장한다.")
        void test1000(){
            // Given
            User user = createUser(Role.SELLER);

            Store store = createStore();

            store.updateUser(user);

            User savedUser = userRepository.save(user);
            Store savedStore = storeRepository.save(store);

            entityManager.flush();
            entityManager.clear();

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

            assertThat(product.getUser().getId()).isEqualTo(savedUser.getId());
            assertThat(product.getStore().getId()).isEqualTo(savedStore.getId());
        }
    }

    @Nested
    @DisplayName("제품 정보 수정 테스트")
    class updateProduct{

        @Test
        @DisplayName("존재하지 않는 제품의 정보를 수정하려고 하면 예외를 반환한다.")
        void test1(){
            // Given
            User user = createUser(Role.SELLER);
            Product product = Product.builder()
                    .name("product name")
                    .description("product description")
                    .build();

            product.updateUser(user);
            userRepository.save(user);

            entityManager.flush();
            entityManager.clear();

            UpdateProductRequest request = UpdateProductRequest.builder()
                    .name("update name")
                    .description("update description")
                    .build();

            //Expected
            assertThatThrownBy(() -> {
                productService.updateProduct(request, 1L, user.getEmail());
            })
                    .isInstanceOf(CustomErrorException.class)
                    .hasMessage("존재하지 않는 제품입니다.");
        }

        @Test
        @DisplayName("제품의 정상적으로 정보를 수정한다.")
        void test1000(){
            // Given
            User user = createUser(Role.SELLER);
            Product product = Product.builder()
                    .name("product name")
                    .description("product description")
                    .build();

            product.updateUser(user);

            userRepository.save(user);
            Product savedProduct = productRepository.save(product);

            entityManager.flush();
            entityManager.clear();

            UpdateProductRequest request = UpdateProductRequest.builder()
                    .name("update name")
                    .description("update description")
                    .build();

            // When
            productService.updateProduct(request, savedProduct.getId(), user.getEmail());

            // Then
            Product foundProduct = productRepository.findById(savedProduct.getId()).orElse(null);
            assertThat(foundProduct).isNotNull();
            assertThat(foundProduct.getName()).isEqualTo("update name");
            assertThat(foundProduct.getDescription()).isEqualTo("update description");
        }
    }

    private Store createStore() {
        Store store = Store.builder()
                .name("store name")
                .description("store description")
                .isAccepted(true)
                .build();
        return store;
    }
    private User createUser(Role role) {
        return User.builder()
                .email("test@naver.com")
                .role(role)
                .password("test123!@")
                .name("test")
                .build();
    }
}