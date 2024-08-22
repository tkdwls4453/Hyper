package com.dev.hyper.item.repository;

import com.dev.hyper.item.Item;
import com.dev.hyper.product.repository.ProductRepository;
import com.dev.hyper.product.domain.Product;
import com.dev.hyper.user.domain.Role;
import com.dev.hyper.user.domain.User;
import com.dev.hyper.user.repository.UserRepository;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@Transactional
@DataJpaTest
class ItemCustomRepositoryImplTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ProductRepository productRepository;

    @AfterEach
    void tearDown() {
        itemRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("productId 로 item 조회")
    void findItemsByProductId(){
        // Given
        Product product = createProduct("product");
        Item item1 = createItem("color1", 10);
        Item item2 = createItem("color2", 10);
        Item item3 = createItem("color3", 10);
        Item item4 = createItem("color4", 10);

        item1.updateProduct(product);
        item2.updateProduct(product);
        item3.updateProduct(product);

        itemRepository.saveAll(List.of(item1, item2, item3, item4));
        Product savedProduct = productRepository.save(product);

        // When
        List<Item> result = itemRepository.findAllByProductId(savedProduct.getId());

        // Then
        assertThat(result)
                .hasSize(3)
                .extracting("color", "stock")
                .containsExactly(
                        Tuple.tuple("color1", 10),
                        Tuple.tuple("color2", 10),
                        Tuple.tuple("color3", 10)
                );
    }

    @Test
    @DisplayName("itemId 와 유저 이메일로 아이템 유무 확인 테스트 - true")
    void existsByIdAndUserEmail_return_true(){
        // Given
        User user = createUser("test@naver.com");
        Product product = createProduct("product");
        Item item = createItem("color", 10);

        product.updateUser(user);
        item.updateProduct(product);

        User savedUser = userRepository.save(user);
        productRepository.save(product);
        Item savedItem = itemRepository.save(item);

        // When
        Boolean result = itemRepository.existsByIdAndUserEmail(savedItem.getId(), savedUser.getEmail());

        // Then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("itemId 와 유저 이메일로 아이템 유무 확인 테스트 - false")
    void existsByIdAndUserEmail_return_false(){
        // Given
        User user = createUser("test@naver.com");
        Product product = createProduct("product");
        Item item = createItem("color", 10);

        product.updateUser(user);
        item.updateProduct(product);

        User savedUser = userRepository.save(user);
        productRepository.save(product);
        Item savedItem = itemRepository.save(item);

        // When
        Boolean result = itemRepository.existsByIdAndUserEmail(savedItem.getId(), "email@naver.com");

        // Then
        assertThat(result).isFalse();
    }

    private User createUser(String email){
        return User.builder()
                .email(email)
                .password("tesag@#$!")
                .role(Role.SELLER)
                .name("user name")
                .build();
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
}