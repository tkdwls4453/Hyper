package com.dev.hyper.order.domain;

import com.dev.hyper.address.AddressInfo;
import com.dev.hyper.common.BaseEntity;
import com.dev.hyper.user.domain.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Table(name = "orders")
@Entity
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int totalPrice;

    private OrderStatus status;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")
    private AddressInfo addressInfo;

    @Builder
    private Order(int totalPrice, OrderStatus status, User user, AddressInfo addressInfo) {
        this.totalPrice = totalPrice;
        this.status = status;
        this.user = user;
        this.addressInfo = addressInfo;
    }

    public void updateTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }
}
