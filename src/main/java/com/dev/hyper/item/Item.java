package com.dev.hyper.item;

import com.dev.hyper.common.BaseEntity;
import com.dev.hyper.common.error.CustomErrorException;
import com.dev.hyper.common.error.ErrorCode;
import com.dev.hyper.item.request.UpdateItemRequest;
import com.dev.hyper.product.domain.Product;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "items")
public class Item extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long  id;

    @Column(length = 50)
    private String color;

    private String size;

    @Column(nullable = false)
    private int stock;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Builder
    private Item(String color, String size, int stock) {
        this.color = color;
        this.size = size;
        this.stock = stock;
    }

    public void updateProduct(Product product) {
        this.product = product;
        product.getItems().add(this);
    }

    public void updateInfo(UpdateItemRequest request) {
        this.color = request.color();
        this.size = request.size();
        this.stock = request.stock();
    }

    public void addStock(int quantity) {
        this.stock += quantity;
    }

    public void reduceStock(int quantity) {
        if (quantity > this.stock) {
            throw new CustomErrorException(ErrorCode.INVALID_STOCK_ERROR);
        }

        this.stock -= quantity;
    }
}
