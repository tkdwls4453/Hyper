package com.dev.hyper.item;

import com.dev.hyper.common.error.CustomErrorException;
import com.dev.hyper.item.repository.ItemRepository;
import com.dev.hyper.item.request.CreateItemRequest;
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
    @Transactional
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
                    .hasMessage("아이템 생성 권한이 없습니다.");
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

            userRepository.save(user);
            productRepository.save(product);

            entityManager.flush();
            entityManager.clear();

            CreateItemRequest request = CreateItemRequest.builder()
                    .color("BLACK")
                    .size("FREE")
                    .stock(10)
                    .build();

            // When
            sut.createItem(request, 1L, user.getEmail());

            // Then
            List<Item> result = itemRepository.findAll();
            assertThat(result).hasSize(1)
                    .extracting("color", "size", "stock")
                    .containsExactly(
                            Tuple.tuple("BLACK", "FREE", 10)
                    );

        }
    }
}