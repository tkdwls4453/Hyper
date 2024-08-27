package com.dev.hyper.product;

import com.dev.hyper.category.Category;
import com.dev.hyper.category.repository.CategoryRepository;
import com.dev.hyper.common.error.CustomErrorException;
import com.dev.hyper.product.domain.Product;
import com.dev.hyper.product.repository.ProductRepository;
import com.dev.hyper.product.request.CreateProductRequest;
import com.dev.hyper.product.request.FilterDto;
import com.dev.hyper.product.request.UpdateProductRequest;
import com.dev.hyper.product.response.ProductResponse;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("test")
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
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductService sut;

    @PersistenceContext
    private EntityManager entityManager;

    @AfterEach
    void tearDown() {
        productRepository.deleteAllInBatch();
        storeRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
        categoryRepository.deleteAllInBatch();

    }

    @Nested
    @DisplayName("제품 생성 테스트")
    class createProduct{

        @Test
        @DisplayName("판매자가 아닌 유저가 제품을 생성하려고 하면 예외를 반환한다.")
        void test1(){
            // Given
            User user = createUser("test@naver.com", Role.BUYER);

            Store store = createStore();
            Category category = createCategory("category");
            store.updateUser(user);

            categoryRepository.save(category);
            userRepository.save(user);
            storeRepository.save(store);

            CreateProductRequest request = CreateProductRequest.builder()
                    .name("product name")
                    .description("product description")
                    .category("category")
                    .build();

            // Expected
            assertThatThrownBy(() -> {
                sut.createProduct(request, user.getEmail());
            })
                    .isInstanceOf(CustomErrorException.class)
                    .hasMessage("제품 생성 권한이 없습니다.");

        }

        @Test
        @DisplayName("존재하지 않는 카테고리로 제품을 생성하려고 하면 예외를 반환한다.")
        void test2(){
            // Given
            User user = createUser("test@naver.com", Role.SELLER);

            Store store = createStore();
            Category category = createCategory("category");
            store.updateUser(user);

            categoryRepository.save(category);
            userRepository.save(user);
            storeRepository.save(store);

            CreateProductRequest request = CreateProductRequest.builder()
                    .name("product name")
                    .description("product description")
                    .category("NO")
                    .build();

            // Expected
            assertThatThrownBy(() -> {
                sut.createProduct(request, user.getEmail());
            })
                    .isInstanceOf(CustomErrorException.class)
                    .hasMessage("존재하지 않는 카테고리 입니다.");

        }

        @Test
        @DisplayName("제품을 생성하여 저장한다.")
        void test1000(){
            // Given
            User user = createUser("test@naver.com", Role.SELLER);

            Store store = createStore();
            Category category = createCategory("category");

            store.updateUser(user);

            categoryRepository.save(category);
            User savedUser = userRepository.save(user);
            Store savedStore = storeRepository.save(store);

            CreateProductRequest request = CreateProductRequest.builder()
                    .name("product name")
                    .description("product description")
                    .category("category")
                    .build();

            // When
            sut.createProduct(request, user.getEmail());

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
            User user = createUser("tset@naver.com", Role.SELLER);
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
                sut.updateProduct(request, 1L, user.getEmail());
            })
                    .isInstanceOf(CustomErrorException.class)
                    .hasMessage("존재하지 않는 제품입니다.");
        }

        @Test
        @DisplayName("존재하지 않는 카테고리로 수정하려고 하면 예외를 반환한다.")
        void test2(){
            // Given
            Category category = createCategory("category");

            User user = createUser("test@naver.com", Role.SELLER);
            Product product = Product.builder()
                    .name("product name")
                    .description("product description")
                    .build();

            product.updateUser(user);
            userRepository.save(user);
            categoryRepository.save(category);
            Product savedProduct = productRepository.save(product);

            UpdateProductRequest request = UpdateProductRequest.builder()
                    .name("update name")
                    .description("update description")
                    .category("NO")
                    .build();

            //Expected
            assertThatThrownBy(() -> {
                sut.updateProduct(request, savedProduct.getId(), user.getEmail());
            })
                    .isInstanceOf(CustomErrorException.class)
                    .hasMessage("존재하지 않는 카테고리 입니다.");
        }

        @Test
        @DisplayName("제품의 정상적으로 정보를 수정한다.")
        void test1000(){
            // Given
            User user = createUser("test@naver.com", Role.SELLER);
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
            sut.updateProduct(request, savedProduct.getId(), user.getEmail());

            // Then
            Product foundProduct = productRepository.findById(savedProduct.getId()).orElse(null);
            assertThat(foundProduct).isNotNull();
            assertThat(foundProduct.getName()).isEqualTo("update name");
            assertThat(foundProduct.getDescription()).isEqualTo("update description");
        }
    }

    @Nested
    @DisplayName("제품 삭제")
    class deleteProduct {

        @Test
        @DisplayName("권한이 없는 유저가 제품을 삭제시, 예외가 발생한다.")
        void test1() {
            // Given
            User user = createUser("test@naver.com", Role.SELLER);
            User otherUser = createUser("email@naver.com", Role.SELLER);
            Product product = Product.builder()
                    .name("product")
                    .description("description")
                    .user(user)
                    .build();

            User savedUser = userRepository.save(user);
            User savedOtherUser = userRepository.save(otherUser);
            Product savedProduct = productRepository.save(product);

            // Expected
            assertThatThrownBy(
                    () -> {
                        sut.deleteProduct(savedProduct.getId(), savedOtherUser.getEmail());
                    }
            )
                    .isInstanceOf(CustomErrorException.class)
                    .hasMessage("제품 접근 권한이 없습니다.");
        }

        @Test
        @DisplayName("존재하지 않는 제품을 삭제시, 예외가 발생한다.")
        void test2(){
            // Given
            User user = createUser("test@naver.com", Role.SELLER);
            Product product = Product.builder()
                    .name("product")
                    .description("description")
                    .user(user)
                    .build();

            User savedUser = userRepository.save(user);
            Product savedProduct = productRepository.save(product);

            // Expected
            assertThatThrownBy(
                    () -> {
                        sut.deleteProduct(savedProduct.getId()+ 100, savedUser.getEmail());
                    }
            )
                    .isInstanceOf(CustomErrorException.class)
                    .hasMessage("존재하지 않는 제품입니다.");
        }

        @Test
        @DisplayName("제품을 정상적으로 삭제한다.")
        void test1000(){
            // Given
            User user = createUser("test@naver.com", Role.SELLER);
            Product product = Product.builder()
                    .name("product")
                    .description("description")
                    .user(user)
                    .build();

            User savedUser = userRepository.save(user);
            Product savedProduct = productRepository.save(product);

            // When
            sut.deleteProduct(savedProduct.getId(), savedUser.getEmail());

            // Then
            Product result = productRepository.findById(savedProduct.getId()).orElse(null);
            assertThat(result).isNull();
        }
    }

    @Nested
    @DisplayName("판매자가 생성한 제품을 정렬 조건을 적용하여 조회한다.")
    class getProducts{
        @Test
        @DisplayName("가격이 비싼 순서로 제품을 10개 조회한다.")
        void test1(){
            // Given
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
                productRepository.save(Product.builder()
                        .name("product" + i)
                        .description("description")
                        .price(i * 1000)
                        .user(user)
                        .category(category)
                        .build());
            }

            PageRequest pageable = PageRequest.of(0, 5);
            String sortingCondition = "expensive";
            String email = "test@naver.com";

            // When
            Page<ProductResponse> result = sut.getProducts(pageable, sortingCondition, email);

            // Then
            List<ProductResponse> content = result.getContent();
            long total = result.getTotalElements();

            assertThat(content).hasSize(5)
                    .extracting("name", "price")
                    .containsExactly(
                            Tuple.tuple("product100", "100,000"),
                            Tuple.tuple("product99", "99,000"),
                            Tuple.tuple("product98", "98,000"),
                            Tuple.tuple("product97", "97,000"),
                            Tuple.tuple("product96", "96,000")
                    );
        }
    }

    @Nested
    @DisplayName("제품을 검색, 필터링 한다.")
    class searchProducts {
        @Test
        @DisplayName("검색어를 포함한 제품을 조회한다.")
        void test1() {
            // Given
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
                productRepository.save(Product.builder()
                        .name("product" + i)
                        .description("description")
                        .price(i * 1000)
                        .user(user)
                        .category(category)
                        .build());
            }

            PageRequest pageable = PageRequest.of(0, 5);
            String search = "2";
            // When
            Page<ProductResponse> result = sut.searchProducts(search, null, null, pageable);

            // Then
            List<ProductResponse> content = result.getContent();
            long total = result.getTotalElements();

            assertThat(content).hasSize(5)
                    .extracting("name", "price")
                    .containsExactly(
                            Tuple.tuple("product2", "2,000"),
                            Tuple.tuple("product12", "12,000"),
                            Tuple.tuple("product20", "20,000"),
                            Tuple.tuple("product21", "21,000"),
                            Tuple.tuple("product22", "22,000")
                    );
        }

        @Test
        @DisplayName("검색어를 포함한 제품을 비싼 순으로 조회한다.")
        void test2() {
            // Given
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
                productRepository.save(Product.builder()
                        .name("product" + i)
                        .description("description")
                        .price(i * 1000)
                        .user(user)
                        .category(category)
                        .build());
            }

            PageRequest pageable = PageRequest.of(0, 5);
            String search = "2";
            String sort = "expensive";
            // When
            Page<ProductResponse> result = sut.searchProducts(search, sort, null, pageable);

            // Then
            List<ProductResponse> content = result.getContent();
            long total = result.getTotalElements();

            assertThat(content).hasSize(5)
                    .extracting("name", "price")
                    .containsExactly(
                            Tuple.tuple("product92", "92,000"),
                            Tuple.tuple("product82", "82,000"),
                            Tuple.tuple("product72", "72,000"),
                            Tuple.tuple("product62", "62,000"),
                            Tuple.tuple("product52", "52,000")
                    );
        }

        @Test
        @DisplayName("검색어를 포함한 제품을 특정 조건을 갖고 비싼 순으로 조회한다.")
        void test3() {
            // Given
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
                productRepository.save(Product.builder()
                        .name("product" + i)
                        .description("description")
                        .price(i * 1000)
                        .user(user)
                        .category(category)
                        .build());
            }

            PageRequest pageable = PageRequest.of(0, 5);
            String search = "2";
            String sort = "expensive";

            FilterDto filterDto = FilterDto.builder()
                    .priceRange(List.of(50000,80000))
                    .build();
            // When
            Page<ProductResponse> result = sut.searchProducts(search, sort, filterDto, pageable);

            // Then
            List<ProductResponse> content = result.getContent();
            long total = result.getTotalElements();

            assertThat(content).hasSize(3)
                    .extracting("name", "price")
                    .containsExactly(
                            Tuple.tuple("product72", "72,000"),
                            Tuple.tuple("product62", "62,000"),
                            Tuple.tuple("product52", "52,000")
                    );
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
    private User createUser(String email, Role role) {
        return User.builder()
                .email(email)
                .role(role)
                .password("test123!@")
                .name("test")
                .build();
    }

    private Category createCategory(String category) {
        return Category.builder()
                .name(category)
                .build();
    }
}