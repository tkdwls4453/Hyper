package com.dev.hyper.item;

import com.dev.hyper.common.BaseEntity;
import com.dev.hyper.product.domain.Product;
import jakarta.persistence.*;

@Entity
@Table(name = "items")
public class Item extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long  id;

    @Column(length = 50)
    private String color;

    private int size;

    @Column(nullable = false)
    private int stock;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

}
