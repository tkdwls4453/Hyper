package com.dev.hyper.item;

import com.dev.hyper.common.error.CustomErrorException;
import com.dev.hyper.item.repository.ItemRepository;
import com.dev.hyper.item.request.AddStockRequest;
import com.dev.hyper.item.request.CreateItemRequest;
import com.dev.hyper.item.request.UpdateItemRequest;
import com.dev.hyper.item.response.ItemResponse;
import com.dev.hyper.product.ProductRepository;
import com.dev.hyper.product.domain.Product;
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


@Transactional
@SpringBootTest(properties = "JWT_SECRET=lalkwfmawlifawnfoiawnfioawfnafkslgnaw")
class ItemServiceTest {

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemService sut;

    @AfterEach
    void tearDown() {
        itemRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @Nested
    @DisplayName("아이템 생성 테스트")
    class createItem{

        @Test
        @DisplayName("존재하지 않는 제품으로 아이템 생성시, 예외를 반환한다.")
        void test1(){
            // Given
            User user = User.builder()
                    .name("test name")
                    .password("testflacw4!@")
                    .email("test@naver.com")
                    .role(Role.SELLER)
                    .build();

            Product product = Product.builder()
                    .name("product name")
                    .description("product description")
                    .user(user)
                    .build();

            User savedUser = userRepository.save(user);
            Product savedProduct = productRepository.save(product);

            entityManager.flush();
            entityManager.clear();

            CreateItemRequest request = CreateItemRequest.builder()
                    .color("BLACK")
                    .size("FREE")
                    .stock(10)
                    .build();

            // Expected
            assertThatThrownBy(
                    () -> {
                        sut.createItem(request, savedProduct.getId() + 1L, user.getEmail());
                    }
            )
                    .isInstanceOf(CustomErrorException.class)
                    .hasMessage("존재하지 않는 제품입니다.");

        }

        @Test
        @DisplayName("유저가 접근하지 못하는 제품으로 아이템 생성시, 예외를 반환한다.")
        void test2(){
            // Given
            User user = User.builder()
                    .name("test name")
                    .password("testflacw4!@")
                    .email("test@naver.com")
                    .role(Role.SELLER)
                    .build();

            Product product = Product.builder()
                    .name("product name")
                    .description("product description")
                    .user(user)
                    .build();

            User savedUser = userRepository.save(user);
            Product savedProduct = productRepository.save(product);

            entityManager.flush();
            entityManager.clear();

            CreateItemRequest request = CreateItemRequest.builder()
                    .color("BLACK")
                    .size("FREE")
                    .stock(10)
                    .build();

            // Expected
            assertThatThrownBy(
                    () -> {
                        sut.createItem(request, savedProduct.getId(), "user@test.com");
                    }
            )
                    .isInstanceOf(CustomErrorException.class)
                    .hasMessage("아이템 접근 권한이 없습니다.");
        }

        @Test
        @DisplayName("정상적으로 아이템 생성시, 생성하여 데이터베이스에 저장한다.")
        void test1000(){
            // Given
            User user = User.builder()
                    .name("test name")
                    .password("testflacw4!@")
                    .email("test@naver.com")
                    .role(Role.SELLER)
                    .build();

            Product product = Product.builder()
                    .name("product name")
                    .description("product description")
                    .user(user)
                    .build();

            User savedUser = userRepository.save(user);
            Product savedProduct = productRepository.save(product);

            CreateItemRequest request = CreateItemRequest.builder()
                    .color("BLACK")
                    .size("FREE")
                    .stock(10)
                    .build();

            // When
            sut.createItem(request, savedProduct.getId(), savedUser.getEmail());

            // Then
            List<Item> result = itemRepository.findAll();
            assertThat(result).hasSize(1)
                    .extracting("color", "size", "stock")
                    .containsExactly(
                            Tuple.tuple("BLACK", "FREE", 10)
                    );

        }
    }

    @Nested
    @DisplayName("제품 아이디로 아이템 조회 테스트")
    class findItemByProductId{
        @Test
        @DisplayName("존재하지 않는 제품 아이디로 아이템 조회시, 예외를 반환한다.")
        void test1(){
            // Given
            User user = createUser("test@naver.com");

            Product product = createProduct("product name");
            Item item1 = createItem("color1", 10);
            Item item2 = createItem("color2", 10);
            Item item3 = createItem("color3", 10);

            product.updateUser(user);

            item1.updateProduct(product);
            item2.updateProduct(product);
            item3.updateProduct(product);

            User savedUser = userRepository.save(user);
            Product savedProduct = productRepository.save(product);
            itemRepository.saveAll(List.of(item1, item2, item3));


            // Expected

            assertThatThrownBy(
                    () -> {
                        sut.getItems(savedProduct.getId() + 100, savedUser.getEmail());
                    }
            )
                    .isInstanceOf(CustomErrorException.class)
                    .hasMessage("존재하지 않는 제품입니다.");
        }

        @Test
        @DisplayName("유저가 접근하지 못하는 제품으로 아이템 조회시, 예외를 반환한다.")
        void test2(){
            // Given
            User user = createUser("test@naver.com");

            Product product = createProduct("product name");
            Item item1 = createItem("color1", 10);
            Item item2 = createItem("color2", 10);
            Item item3 = createItem("color3", 10);

            product.updateUser(user);

            item1.updateProduct(product);
            item2.updateProduct(product);
            item3.updateProduct(product);

            userRepository.save(user);
            Product savedProduct = productRepository.save(product);
            itemRepository.saveAll(List.of(item1, item2, item3));


            // Expected

            assertThatThrownBy(
                    () -> {
                        sut.getItems(savedProduct.getId(), "email@naver.com");
                    }
            )
                    .isInstanceOf(CustomErrorException.class)
                    .hasMessage("아이템 접근 권한이 없습니다.");
        }

        @Test
        @DisplayName("제품 아이디로 아이템 조회시, 아이템들을 반환한다.")
        void test1000(){
            // Given
            User user = createUser("test@naver.com");

            Product product = createProduct("product name");
            Item item1 = createItem("color1", 10);
            Item item2 = createItem("color2", 10);
            Item item3 = createItem("color3", 10);

            product.updateUser(user);

            item1.updateProduct(product);
            item2.updateProduct(product);
            item3.updateProduct(product);

            User savedUser = userRepository.save(user);
            Product savedProduct = productRepository.save(product);
            itemRepository.saveAll(List.of(item1, item2, item3));

            // When
            List<ItemResponse> result = sut.getItems(savedProduct.getId(), savedUser.getEmail());

            // Then
            assertThat(result).hasSize(3)
                    .extracting("color", "stock")
                    .containsExactlyInAnyOrder(
                            Tuple.tuple("color1", 10),
                            Tuple.tuple("color2", 10),
                            Tuple.tuple("color3", 10)
                    );
        }
    }

    @Nested
    @DisplayName("아이템 수정 테스트")
    class updateItem{
        @Test
        @DisplayName("아이템 수정 권한이 없는 유저가 아이템 수정을 요청시, 예외를 반환한다.")
        void test1(){
            // Given
            User user = createUser("test@naver.com");
            Product product = createProduct("product");
            Item item = createItem("color", 10);

            product.updateUser(user);
            item.updateProduct(product);

            User savedUser = userRepository.save(user);
            Product savedProduct = productRepository.save(product);
            Item savedItem = itemRepository.save(item);

            UpdateItemRequest request = UpdateItemRequest.builder()
                    .color("update color")
                    .size("XL")
                    .stock(5)
                    .build();

            // Expected
            assertThatThrownBy(
                    () -> {
                        sut.updateInfo(savedItem.getId(), request, "email@naver.com");
                    }
            )
                    .isInstanceOf(CustomErrorException.class)
                    .hasMessage("아이템 접근 권한이 없습니다.");
        }

        @Test
        @DisplayName("존재하지 않는 아이템으 아이템 수정을 요청시, 예외를 반환한다.")
        void test2(){
            // Given
            User user = createUser("test@naver.com");
            Product product = createProduct("product");
            Item item = createItem("color", 10);

            product.updateUser(user);
            item.updateProduct(product);

            User savedUser = userRepository.save(user);
            Product savedProduct = productRepository.save(product);
            Item savedItem = itemRepository.save(item);

            UpdateItemRequest request = UpdateItemRequest.builder()
                    .color("update color")
                    .size("XL")
                    .stock(5)
                    .build();

            // Expected
            assertThatThrownBy(
                    () -> {
                        sut.updateInfo(savedItem.getId() + 100, request, savedUser.getEmail());
                    }
            )
                    .isInstanceOf(CustomErrorException.class)
                    .hasMessage("존재하지 않는 아이템입니다.");
        }

        @Test
        @DisplayName("정상적으로 아이템 수정을 요청하면, 아이템을 수정한다.")
        void test1000(){
            // Given
            User user = createUser("test@naver.com");
            Product product = createProduct("product");
            Item item = createItem("color", 10);

            product.updateUser(user);
            item.updateProduct(product);

            User savedUser = userRepository.save(user);
            Product savedProduct = productRepository.save(product);
            Item savedItem = itemRepository.save(item);

            UpdateItemRequest request = UpdateItemRequest.builder()
                    .color("update color")
                    .size("XL")
                    .stock(5)
                    .build();

            // When
            sut.updateInfo(savedItem.getId(), request, user.getEmail());

            // Then
            Item result = itemRepository.findById(savedItem.getId()).orElse(null);

            assertThat(result).isNotNull()
                    .extracting("color", "size", "stock")
                    .containsExactly("update color", "XL", 5);
        }
    }

    @Nested
    @DisplayName("아이템 제거 테스트")
    class deleteItem{
        @Test
        @DisplayName("아이템 수정 권한이 없는 유저가 아이템 삭제 요청시, 예외를 반환한다.")
        void test1(){
            // Given
            User user = createUser("test@naver.com");
            Product product = createProduct("product");
            Item item = createItem("color", 10);

            product.updateUser(user);
            item.updateProduct(product);

            User savedUser = userRepository.save(user);
            Product savedProduct = productRepository.save(product);
            Item savedItem = itemRepository.save(item);

            // Expected
            assertThatThrownBy(
                    () -> {
                        sut.deleteItem(savedItem.getId(), "email@naver.com");
                    }
            )
                    .isInstanceOf(CustomErrorException.class)
                    .hasMessage("아이템 접근 권한이 없습니다.");
        }

        @Test
        @DisplayName("존재하지 않는 아이템으로 아이템 삭제를 요청시, 예외를 반환한다.")
        void test2(){
            // Given
            User user = createUser("test@naver.com");
            Product product = createProduct("product");
            Item item = createItem("color", 10);

            product.updateUser(user);
            item.updateProduct(product);

            User savedUser = userRepository.save(user);
            Product savedProduct = productRepository.save(product);
            Item savedItem = itemRepository.save(item);


            // Expected
            assertThatThrownBy(
                    () -> {
                        sut.deleteItem(savedItem.getId() + 100, savedUser.getEmail());
                    }
            )
                    .isInstanceOf(CustomErrorException.class)
                    .hasMessage("존재하지 않는 아이템입니다.");
        }

        @Test
        @DisplayName("정상적으로 아이템 삭제를 요청하면, 아이템을 수정한다.")
        void test1000(){
            // Given
            User user = createUser("test@naver.com");
            Product product = createProduct("product");
            Item item = createItem("color", 10);

            product.updateUser(user);
            item.updateProduct(product);

            User savedUser = userRepository.save(user);
            Product savedProduct = productRepository.save(product);
            Item savedItem = itemRepository.save(item);

            // When
            sut.deleteItem(savedItem.getId(), user.getEmail());

            // Then
            Item result = itemRepository.findById(savedItem.getId()).orElse(null);

            assertThat(result).isNull();
        }
    }

    @DisplayName("아이템 재고 추가 테스트")
    @Nested
    class addStock{
        @Test
        @DisplayName("아이템 수정 권한이 없는 유저가 재고 추가 요청시, 예외를 반환한다.")
        void test1(){
            // Given
            User user = createUser("test@naver.com");
            Product product = createProduct("product");
            Item item = createItem("color", 10);

            product.updateUser(user);
            item.updateProduct(product);

            User savedUser = userRepository.save(user);
            Product savedProduct = productRepository.save(product);
            Item savedItem = itemRepository.save(item);

            AddStockRequest request = AddStockRequest.builder()
                    .quantity(5)
                    .build();

            // Expected
            assertThatThrownBy(
                    () -> {
                        sut.addStock(savedItem.getId(), request, "email@naver.com");
                    }
            )
                    .isInstanceOf(CustomErrorException.class)
                    .hasMessage("아이템 접근 권한이 없습니다.");
        }

        @Test
        @DisplayName("존재하지 않는 아이템의 재고 추가를 요청시, 예외를 반환한다.")
        void test2(){
            // Given
            User user = createUser("test@naver.com");
            Product product = createProduct("product");
            Item item = createItem("color", 10);

            product.updateUser(user);
            item.updateProduct(product);

            User savedUser = userRepository.save(user);
            Product savedProduct = productRepository.save(product);
            Item savedItem = itemRepository.save(item);

            AddStockRequest request = AddStockRequest.builder()
                    .quantity(5)
                    .build();

            // Expected
            assertThatThrownBy(
                    () -> {
                        sut.addStock(savedItem.getId() + 100, request,  savedUser.getEmail());
                    }
            )
                    .isInstanceOf(CustomErrorException.class)
                    .hasMessage("존재하지 않는 아이템입니다.");
        }

        @Test
        @DisplayName("정상적으로 재고 추가를 요청시, 예외를 반환한다.")
        void test1000(){
            // Given
            User user = createUser("test@naver.com");
            Product product = createProduct("product");
            Item item = createItem("color", 10);

            product.updateUser(user);
            item.updateProduct(product);

            User savedUser = userRepository.save(user);
            Product savedProduct = productRepository.save(product);
            Item savedItem = itemRepository.save(item);

            AddStockRequest request = AddStockRequest.builder()
                    .quantity(5)
                    .build();

            // When
            sut.addStock(savedItem.getId(), request, savedUser.getEmail());

            // Then
            Item result = itemRepository.findById(savedItem.getId()).orElse(null);

            assertThat(result).isNotNull();
            assertThat(result.getStock()).isEqualTo(15);

        }
    }

    private Product createProduct(String name){
        return Product.builder()
                .name(name)
                .description("product description")
                .build();
    }

    private Item createItem(String color, int stock){
        return Item.builder()
                .color(color)
                .size("FREE")
                .stock(stock)
                .build();
    }

    private User createUser(String email){
        return User.builder()
                .name("test")
                .email(email)
                .role(Role.SELLER)
                .password("test123@#")
                .build();
    }
}