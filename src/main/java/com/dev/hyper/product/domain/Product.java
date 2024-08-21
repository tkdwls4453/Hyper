package com.dev.hyper.product.domain;

import com.dev.hyper.category.Category;
import com.dev.hyper.common.BaseEntity;
import com.dev.hyper.item.Item;
import com.dev.hyper.product.request.UpdateProductRequest;
import com.dev.hyper.store.domain.Store;
import com.dev.hyper.user.domain.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Table(name = "products")
@Entity
public class Product extends BaseEntity {

    @Id
    @Column(name = "product_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(length = 500)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "product")
    private List<Item> items = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;


    @Builder
    private Product(String name, String description, Store store, User user) {
        this.name = name;
        this.description = description;
        this.store = store;
        this.user = user;
    }

    public void updateUser(User user) {
        this.user = user;
    }

    public void updateStore(Store store) {
        this.store = store;
    }

    public void updateInfo(UpdateProductRequest request) {
        this.name = request.name();
        this.description = request.description();
    }

    public void updateCategory(Category category) {
        this.category = category;
    }
}
