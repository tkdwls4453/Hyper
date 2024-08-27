package com.dev.hyper.order.service;

import com.dev.hyper.address.AddressInfo;
import com.dev.hyper.common.error.CustomErrorException;
import com.dev.hyper.common.error.ErrorCode;
import com.dev.hyper.item.Item;
import com.dev.hyper.item.repository.ItemRepository;
import com.dev.hyper.order.domain.Order;
import com.dev.hyper.order.domain.OrderItem;
import com.dev.hyper.order.domain.OrderStatus;
import com.dev.hyper.order.repository.OrderItemRepository;
import com.dev.hyper.order.repository.OrderRepository;
import com.dev.hyper.order.request.OrderRequest;
import com.dev.hyper.user.domain.User;
import com.dev.hyper.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Transactional
@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    private final OrderItemRepository orderItemRepository;
    public void createOrder(OrderRequest orderRequest, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> {
                    throw new CustomErrorException(ErrorCode.NOT_FOUND_USER_ERROR);
                }
        );

        AddressInfo address = user.getAddresses().stream().filter(
                        addressInfo -> addressInfo.getId() == orderRequest.addressId()
                )
                .findFirst()
                .get();

        Order order = Order.builder()
                .user(user)
                .addressInfo(address)
                .status(OrderStatus.PAYMENT_COMPLETED)
                .build();

        AtomicInteger totalPrice = new AtomicInteger();

        List<OrderItem> orderItems = new ArrayList<>();
        orderRequest.OrderItems().stream().forEach(
                orderItemInfo -> {
                    Item item = itemRepository.findById(orderItemInfo.itemId()).orElseThrow(
                            () -> {
                                throw new CustomErrorException(ErrorCode.ITEM_NOT_FOUND_ERROR);
                            }
                    );

                    item.reduceStock(orderItemInfo.quantity());

                    OrderItem orderItem = OrderItem.builder()
                            .item(item)
                            .order(order)
                            .quantity(orderItemInfo.quantity())
                            .build();

                    orderItems.add(orderItem);
                    totalPrice.addAndGet(orderItemInfo.quantity() * item.getProduct().getPrice());
                }
        );

        order.updateTotalPrice(totalPrice.get());
        orderRepository.save(order);
        orderItemRepository.saveAll(orderItems);
    }
}
