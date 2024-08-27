package com.dev.hyper.order.service;


import com.dev.hyper.address.AddressInfo;
import com.dev.hyper.address.AddressRepository;
import com.dev.hyper.category.Category;
import com.dev.hyper.category.repository.CategoryRepository;
import com.dev.hyper.common.error.CustomErrorException;
import com.dev.hyper.item.Item;
import com.dev.hyper.item.repository.ItemRepository;
import com.dev.hyper.order.domain.Order;
import com.dev.hyper.order.domain.OrderStatus;
import com.dev.hyper.order.repository.OrderRepository;
import com.dev.hyper.order.request.OrderItemInfo;
import com.dev.hyper.order.request.OrderRequest;
import com.dev.hyper.product.domain.Product;
import com.dev.hyper.product.repository.ProductRepository;
import com.dev.hyper.user.domain.Role;
import com.dev.hyper.user.domain.User;
import com.dev.hyper.user.repository.UserRepository;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("test")
@Transactional
@SpringBootTest(properties = "JWT_SECRET=lalkwfmawlifawnfoiawnfioawfnafkslgnaw")
class OrderServiceTest {

    @Autowired
    private OrderService sut;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CategoryRepository categoryRepository;
    @Nested
    @DisplayName("주문 테스트")
    class createOrder{

        @Test
        @DisplayName("재고가 부족한 아이템을 주문하면 주문되지 않고 예외를 반환한다.")
        void test1(){
            // Given
            User user = createUser("tset@naver.com", Role.BUYER);
            AddressInfo address = createAddress(user);

            userRepository.save(user);
            AddressInfo savedAddress = addressRepository.save(address);

            Category category = createCategory("category");
            categoryRepository.save(category);


            Product product1 = createProduct("product1", category, 5000);
            Product product2 = createProduct("product2", category, 10000);

            productRepository.saveAll(List.of(product1, product2));

            Item item1 = createItem(1, product1);
            Item item2 = createItem(15, product1);

            Item savedItem1 = itemRepository.save(item1);
            Item savedItem2 = itemRepository.save(item2);

            Item item3 = createItem(5, product2);
            Item item4 = createItem(10, product2);

            Item savedItem3 = itemRepository.save(item3);
            Item savedItem4 = itemRepository.save(item4);

            OrderItemInfo orderItemInfo1 = OrderItemInfo.builder()
                    .itemId(savedItem1.getId())
                    .quantity(3)
                    .build();

            OrderItemInfo orderItemInfo2 = OrderItemInfo.builder()
                    .itemId(savedItem3.getId())
                    .quantity(2)
                    .build();


            OrderRequest request = OrderRequest.builder()
                    .addressId(savedAddress.getId())
                    .OrderItems(List.of(orderItemInfo1, orderItemInfo2))
                    .build();

            // Expected
            assertThatThrownBy(
                    () -> {
                        sut.createOrder(request, user.getEmail());
                    }
            )
                    .isInstanceOf(CustomErrorException.class)
                    .hasMessage("유효하지 않는 재고량입니다.");


            // 주문 생성되지 않은 것을 확인
            List<Order> result = orderRepository.findAll();
            assertThat(result).hasSize(0);

            // 재고 감소가 안된 것을 확인
            Item item1Result = itemRepository.findById(savedItem1.getId()).orElse(null);
            assertThat(item1Result).isNotNull();
            assertThat(item1Result.getStock()).isEqualTo(1);

            Item item3Result = itemRepository.findById(savedItem3.getId()).orElse(null);
            assertThat(item3Result).isNotNull();
            assertThat(item3Result.getStock()).isEqualTo(5);
        }

        @Test
        @DisplayName("유저가 배송지와 아이템 정보로 주문을 한다.")
        void test1000(){
            // Given
            User user = createUser("tset@naver.com", Role.BUYER);
            AddressInfo address = createAddress(user);

            userRepository.save(user);
            AddressInfo savedAddress = addressRepository.save(address);

            Category category = createCategory("category");
            categoryRepository.save(category);


            Product product1 = createProduct("product1", category, 5000);
            Product product2 = createProduct("product2", category, 10000);

            productRepository.saveAll(List.of(product1, product2));

            Item item1 = createItem(10, product1);
            Item item2 = createItem(15, product1);

            Item savedItem1 = itemRepository.save(item1);
            Item savedItem2 = itemRepository.save(item2);

            Item item3 = createItem(5, product2);
            Item item4 = createItem(10, product2);

            Item savedItem3 = itemRepository.save(item3);
            Item savedItem4 = itemRepository.save(item4);

            OrderItemInfo orderItemInfo1 = OrderItemInfo.builder()
                    .itemId(savedItem1.getId())
                    .quantity(3)
                    .build();

            OrderItemInfo orderItemInfo2 = OrderItemInfo.builder()
                    .itemId(savedItem3.getId())
                    .quantity(2)
                    .build();


            OrderRequest request = OrderRequest.builder()
                    .addressId(savedAddress.getId())
                    .OrderItems(List.of(orderItemInfo1, orderItemInfo2))
                    .build();

            // When
            sut.createOrder(request, user.getEmail());

            // Then

            // 주문 생성 확인
            List<Order> result = orderRepository.findAll();
            assertThat(result).hasSize(1)
                    .extracting("totalPrice", "status")
                    .containsExactly(
                            Tuple.tuple(5000 * 3 + 10000 * 2, OrderStatus.PAYMENT_COMPLETED)
                    );

            // 재고 감소 확인
            Item item1Result = itemRepository.findById(savedItem1.getId()).orElse(null);
            assertThat(item1Result).isNotNull();
            assertThat(item1Result.getStock()).isEqualTo(10 - 3);

            Item item3Result = itemRepository.findById(savedItem3.getId()).orElse(null);
            assertThat(item3Result).isNotNull();
            assertThat(item3Result.getStock()).isEqualTo(5 - 2);
        }

        private Category createCategory(String category) {
            return Category.builder()
                    .name(category)
                    .build();
        }

        private User createUser(String email, Role role) {
            return User.builder()
                    .email(email)
                    .role(role)
                    .name("test")
                    .password("test!@12")
                    .build();
        }

        private AddressInfo createAddress(User user) {
            return AddressInfo.builder()
                    .title("address")
                    .user(user)
                    .address("address")
                    .addressDetail("address detail")
                    .code("11111")
                    .build();
        }

        private Product createProduct(String name, Category category, int price){

            return Product.builder()
                    .name(name)
                    .category(category)
                    .description("description")
                    .price(price)
                    .build();
        }

        private Item createItem(int stock, Product product) {
            Item item = Item.builder()
                    .color("BLACK")
                    .size("XL")
                    .stock(stock)
                    .build();

            item.updateProduct(product);

            return item;
        }
    }
}