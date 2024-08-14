package com.dev.hyper.store.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity
@Table(name = "stores")
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private boolean isAccepted;

    private String logo;

    @Builder
    private Store(String name, String description, boolean isAccepted, String logo) {
        this.name = name;
        this.description = description;
        this.isAccepted = isAccepted;
        this.logo = logo;
    }
}
